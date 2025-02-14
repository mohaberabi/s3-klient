package com.mohaberabi.s3klient

import cocoapods.AWSCore.AWSRegionType
import cocoapods.AWSS3.AWSS3TransferUtilityTransferStatusType
import kotlinx.cinterop.ExperimentalForeignApi


@OptIn(ExperimentalForeignApi::class)
internal fun AWSS3TransferUtilityTransferStatusType.toUploadResult(
    errorMessage: String? = null
): UploadFileResult = when (this) {
    AWSS3TransferUtilityTransferStatusType.AWSS3TransferUtilityTransferStatusCancelled ->
        UploadFileResult.Canceled(message = errorMessage ?: "Upload was canceled")

    AWSS3TransferUtilityTransferStatusType.AWSS3TransferUtilityTransferStatusError ->
        UploadFileResult.DidNotComplete(message = errorMessage ?: "Upload failed")

    AWSS3TransferUtilityTransferStatusType.AWSS3TransferUtilityTransferStatusPaused,
    AWSS3TransferUtilityTransferStatusType.AWSS3TransferUtilityTransferStatusWaiting,
    AWSS3TransferUtilityTransferStatusType.AWSS3TransferUtilityTransferStatusInProgress,
        -> UploadFileResult.DidNotComplete(message = "Upload did not complete, status: ${this.name}")

    else -> UploadFileResult.Unknown
}

@OptIn(ExperimentalForeignApi::class)
fun CognitoRegion.asIosAwsRegion() = when (this) {
    CognitoRegion.GovCloud -> AWSRegionType.AWSRegionUSGovEast1
    CognitoRegion.US_GOV_EAST_1 -> AWSRegionType.AWSRegionUSGovEast1
    CognitoRegion.US_EAST_1 -> AWSRegionType.AWSRegionUSEast1
    CognitoRegion.US_EAST_2 -> AWSRegionType.AWSRegionUSEast2
    CognitoRegion.US_WEST_1 -> AWSRegionType.AWSRegionUSWest1
    CognitoRegion.US_WEST_2 -> AWSRegionType.AWSRegionUSWest2
    CognitoRegion.EU_SOUTH_1 -> AWSRegionType.AWSRegionEUSouth1
    CognitoRegion.EU_WEST_1 -> AWSRegionType.AWSRegionEUWest1
    CognitoRegion.EU_WEST_2 -> AWSRegionType.AWSRegionEUWest2
    CognitoRegion.EU_WEST_3 -> AWSRegionType.AWSRegionEUWest3
    CognitoRegion.EU_CENTRAL_1 -> AWSRegionType.AWSRegionEUCentral1
    CognitoRegion.EU_NORTH_1 -> AWSRegionType.AWSRegionEUNorth1
    CognitoRegion.AP_EAST_1 -> AWSRegionType.AWSRegionAPEast1
    CognitoRegion.AP_SOUTH_1 -> AWSRegionType.AWSRegionAPSouth1
    CognitoRegion.AP_SOUTHEAST_1 -> AWSRegionType.AWSRegionAPSoutheast1
    CognitoRegion.AP_SOUTHEAST_2 -> AWSRegionType.AWSRegionAPSoutheast2
    CognitoRegion.AP_NORTHEAST_1 -> AWSRegionType.AWSRegionAPNortheast1
    CognitoRegion.AP_NORTHEAST_2 -> AWSRegionType.AWSRegionAPNortheast2
    CognitoRegion.SA_EAST_1 -> AWSRegionType.AWSRegionSAEast1
    CognitoRegion.CA_CENTRAL_1 -> AWSRegionType.AWSRegionCACentral1
    CognitoRegion.CN_NORTH_1 -> AWSRegionType.AWSRegionCNNorth1
    CognitoRegion.CN_NORTHWEST_1 -> AWSRegionType.AWSRegionCNNorthWest1
    CognitoRegion.ME_SOUTH_1 -> AWSRegionType.AWSRegionMESouth1
    CognitoRegion.AF_SOUTH_1 -> AWSRegionType.AWSRegionAFSouth1
    CognitoRegion.AP_SOUTHEAST_3 -> AWSRegionType.AWSRegionAPSoutheast3
}