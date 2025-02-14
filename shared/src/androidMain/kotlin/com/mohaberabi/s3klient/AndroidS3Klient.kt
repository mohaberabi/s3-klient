package com.mohaberabi.s3klient

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.lang.Exception
import kotlin.coroutines.resume


internal class AndroidS3Klient(
    private val context: Context,
    private val credentials: S3KlientCredentials,
) : S3Klient {
    private val s3Client by lazy {
        val client = AmazonS3Client(sCredProvider)
        client.setRegion(Region.getRegion(credentials.cognitoRegion.asAndroidAwsRegion()))
        client
    }

    private val transferUtility: TransferUtility by lazy {
        TransferUtility.builder()
            .s3Client(s3Client)
            .context(context)
            .build()
    }


    private val sCredProvider by lazy {
        CognitoCachingCredentialsProvider(
            context,
            credentials.cognitoIdentityId,
            credentials.cognitoRegion.asAndroidAwsRegion()
        )
    }

    override suspend fun uploadFile(
        request: AwsFileUploadRequest,
    ): UploadFileResult {

        return suspendCancellableCoroutine { continuation ->

            val file = File(request.filePath)
            val uploadObserver = transferUtility.upload(
                request.bucket,
                request.filePath,
                file
            )

            val transferListener = object : TransferListener {
                override fun onStateChanged(id: Int, state: TransferState?) {
                    when (state) {
                        TransferState.COMPLETED -> {
                            val fileUrl =
                                "https://${request.bucket}.s3.amazonaws.com/${request.filePath}"
                            continuation.resume(UploadFileResult.Uploaded(path = fileUrl, id = id))
                        }

                        else -> {
                            if (state != null) {
                                continuation.resume(state.toUploadResult(id))
                            } else {
                                continuation.resume(UploadFileResult.Unknown)
                            }
                        }
                    }
                }

                override fun onProgressChanged(
                    id: Int,
                    bytesCurrent: Long, bytesTotal: Long
                ) {

                }

                override fun onError(id: Int, ex: Exception?) {
                    continuation.resume(
                        UploadFileResult.Error(
                            message = ex?.message,
                            cause = ex
                        )
                    )
                }

            }

            uploadObserver.setTransferListener(transferListener)
        }
    }


}

internal fun TransferState.toUploadResult(id: Int): UploadFileResult {
    return when (this) {
        TransferState.CANCELED -> UploadFileResult.Canceled(message = "Upload was canceled , ID:$id")
        TransferState.FAILED -> UploadFileResult.DidNotComplete(message = "Upload failed , I:$id")
        TransferState.WAITING_FOR_NETWORK -> UploadFileResult.DidNotComplete(message = "Waiting for network , ID:$id")
        TransferState.IN_PROGRESS, TransferState.WAITING -> UploadFileResult.DidNotComplete(message = "Still uploading, ID:$id")
        else -> UploadFileResult.Unknown
    }
}

class AndroidS3KlientFactory(
    private val context: Context,
    private val credentials: S3KlientCredentials
) : S3KlientFactory {
    override fun create(): S3Klient = AndroidS3Klient(context, credentials)

}