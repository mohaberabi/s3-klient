package com.mohaberabi.s3klient


sealed interface UploadFileResult {

    data class Canceled(
        val message: String? = null,
    ) : UploadFileResult

    data object Unknown : UploadFileResult

    data class Uploaded(
        val path: String,
        val id: Int? = null,
    ) : UploadFileResult

    data class DidNotComplete(
        val message: String? = null,
    ) : UploadFileResult

    data class Error(
        val message: String? = null,
        val cause: Throwable? = null
    ) : UploadFileResult

}

data class S3KlientCredentials(
    val cognitoIdentityId: String = "",
    val cognitoRegion: CognitoRegion,
)

data class AwsFileUploadRequest(
    val filePath: String,
    val bucket: String = "",
    val contentType: String = "",
)

enum class CognitoRegion(val id: String) {
    GovCloud("us-gov-west-1"),
    US_GOV_EAST_1("us-gov-east-1"),
    US_EAST_1("us-east-1"),
    US_EAST_2("us-east-2"),
    US_WEST_1("us-west-1"),
    US_WEST_2("us-west-2"),
    EU_SOUTH_1("eu-south-1"),
    EU_WEST_1("eu-west-1"),
    EU_WEST_2("eu-west-2"),
    EU_WEST_3("eu-west-3"),
    EU_CENTRAL_1("eu-central-1"),
    EU_NORTH_1("eu-north-1"),
    AP_EAST_1("ap-east-1"),
    AP_SOUTH_1("ap-south-1"),
    AP_SOUTHEAST_1("ap-southeast-1"),
    AP_SOUTHEAST_2("ap-southeast-2"),
    AP_NORTHEAST_1("ap-northeast-1"),
    AP_NORTHEAST_2("ap-northeast-2"),
    SA_EAST_1("sa-east-1"),
    CA_CENTRAL_1("ca-central-1"),
    CN_NORTH_1("cn-north-1"),
    CN_NORTHWEST_1("cn-northwest-1"),
    ME_SOUTH_1("me-south-1"),
    AF_SOUTH_1("af-south-1"),
    AP_SOUTHEAST_3("ap-southeast-3");
}

fun interface S3KlientFactory {
    fun create(): S3Klient
}


interface S3Klient {
    suspend fun uploadFile(request: AwsFileUploadRequest): UploadFileResult
}