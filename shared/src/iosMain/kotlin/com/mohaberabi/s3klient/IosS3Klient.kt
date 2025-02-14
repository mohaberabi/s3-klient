package com.mohaberabi.s3klient

import cocoapods.AWSCore.AWSCognitoCredentialsProvider
import cocoapods.AWSCore.AWSServiceConfiguration
import cocoapods.AWSCore.AWSServiceManager
import cocoapods.AWSS3.AWSS3TransferUtility
import cocoapods.AWSS3.AWSS3TransferUtilityUploadExpression
import cocoapods.AWSS3.AWSS3TransferUtilityUploadTask
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSLog
import platform.Foundation.NSURL
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
internal class IosS3Klient(
    private val credentials: S3KlientCredentials,
) : S3Klient {


    private val credentialsProvider = AWSCognitoCredentialsProvider(
        regionType = credentials.cognitoRegion.asIosAwsRegion(),
        identityPoolId = credentials.cognitoIdentityId
    )

    private val configuration = AWSServiceConfiguration(
        credentials.cognitoRegion.asIosAwsRegion(),
        credentialsProvider
    )

    init {
        AWSServiceManager.defaultServiceManager()?.defaultServiceConfiguration = configuration
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun uploadFile(request: AwsFileUploadRequest): UploadFileResult {
        val file = NSURL.fileURLWithPath(request.filePath)
        val transferUtility = AWSS3TransferUtility.defaultS3TransferUtility()
        val expression = AWSS3TransferUtilityUploadExpression()
        return suspendCancellableCoroutine { continuation ->
            try {
                val task = transferUtility.uploadFile(
                    fileURL = file,
                    bucket = request.bucket,
                    contentType = request.contentType,
                    expression = expression,
                    key = "invalid",
                    completionHandler = null
                )

                task.continueWithBlock { awsTask ->
                    val errorMessage = awsTask?.error()?.localizedDescription ?: "Unknown AWS Error"
                    when {
                        awsTask == null -> continuation.resume(UploadFileResult.Unknown)
                        awsTask.isCancelled() -> continuation.resume(
                            UploadFileResult.Canceled(
                                message = errorMessage
                            )
                        )

                        awsTask.isFaulted() -> continuation.resume(
                            UploadFileResult.DidNotComplete(
                                message = errorMessage
                            )
                        )

                        else -> {
                            if (awsTask.isCompleted()) {
                                val uploadTask = awsTask.result() as? AWSS3TransferUtilityUploadTask
                                val fileUrl =
                                    "https://${request.bucket}.s3.amazonaws.com/${request.filePath}"
                                val id = uploadTask?.taskIdentifier?.toInt()
                                NSLog("${S3_KLIENT_TAG}:fileUrl")
                                NSLog("${S3_KLIENT_TAG}:Upload Task ID: ${uploadTask?.taskIdentifier}")
                                NSLog("${S3_KLIENT_TAG}:Upload State: ${uploadTask?.status}")
                                continuation.resume(
                                    UploadFileResult.Uploaded(
                                        path = fileUrl,
                                        id = id
                                    )
                                )
                            } else {
                                val uploadTask = awsTask.result() as? AWSS3TransferUtilityUploadTask
                                uploadTask?.status?.let { continuation.resume(it.toUploadResult()) }
                                    ?: run { continuation.resume(UploadFileResult.Unknown) }

                            }
                        }

                    }
                }

            } catch (e: Exception) {
                if (e is CancellationException) throw e
                continuation.resume(UploadFileResult.Error(cause = e, message = e.message))
                e.printStackTrace()

            }
        }
    }
}


class IosS3KlientFactory(
    private val credentials: S3KlientCredentials,
) : S3KlientFactory {
    override fun create(
    ): S3Klient = IosS3Klient(credentials)
}