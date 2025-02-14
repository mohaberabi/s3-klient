package com.mohaberabi.s3klient


import com.amazonaws.regions.Regions

fun CognitoRegion.asAndroidAwsRegion(): Regions {
    return when (this) {
        CognitoRegion.GovCloud -> Regions.US_GOV_EAST_1
        CognitoRegion.US_GOV_EAST_1 -> Regions.US_GOV_EAST_1
        CognitoRegion.US_EAST_1 -> Regions.US_EAST_1
        CognitoRegion.US_EAST_2 -> Regions.US_EAST_2
        CognitoRegion.US_WEST_1 -> Regions.US_WEST_1
        CognitoRegion.US_WEST_2 -> Regions.US_WEST_2
        CognitoRegion.EU_SOUTH_1 -> Regions.EU_SOUTH_1
        CognitoRegion.EU_WEST_1 -> Regions.EU_WEST_1
        CognitoRegion.EU_WEST_2 -> Regions.EU_WEST_2
        CognitoRegion.EU_WEST_3 -> Regions.EU_WEST_3
        CognitoRegion.EU_CENTRAL_1 -> Regions.EU_CENTRAL_1
        CognitoRegion.EU_NORTH_1 -> Regions.EU_NORTH_1
        CognitoRegion.AP_EAST_1 -> Regions.AP_EAST_1
        CognitoRegion.AP_SOUTH_1 -> Regions.AP_SOUTH_1
        CognitoRegion.AP_SOUTHEAST_1 -> Regions.AP_SOUTHEAST_1
        CognitoRegion.AP_SOUTHEAST_2 -> Regions.AP_SOUTHEAST_2
        CognitoRegion.AP_NORTHEAST_1 -> Regions.AP_NORTHEAST_1
        CognitoRegion.AP_NORTHEAST_2 -> Regions.AP_NORTHEAST_2
        CognitoRegion.SA_EAST_1 -> Regions.SA_EAST_1
        CognitoRegion.CA_CENTRAL_1 -> Regions.CA_CENTRAL_1
        CognitoRegion.CN_NORTH_1 -> Regions.CN_NORTH_1
        CognitoRegion.CN_NORTHWEST_1 -> Regions.CN_NORTHWEST_1
        CognitoRegion.ME_SOUTH_1 -> Regions.ME_SOUTH_1
        CognitoRegion.AF_SOUTH_1 -> Regions.AF_SOUTH_1
        CognitoRegion.AP_SOUTHEAST_3 -> Regions.AP_SOUTHEAST_3
        else -> Regions.DEFAULT_REGION
    }
}