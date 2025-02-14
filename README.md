# S3Klient - AWS S3 File Upload for Kotlin Multiplatform

S3Klient is a Kotlin Multiplatform (KMP) library that allows seamless file uploads to AWS S3 on *
*Android** and **iOS**. It utilizes the AWS SDK for both platforms, ensuring a unified API for file
uploads. using each platform native sdk from Amazon

## 🚀 Features

- **Kotlin Multiplatform (KMP)** support
- Uses **AWS S3 Transfer Utility** for efficient uploads
- Supports **Android (AWS SDK)** and **iOS (CocoaPods AWS SDK)**
- Provides **suspend functions** for easy coroutine-based uploads

---

## 📦 Installation

### **1️⃣ Add Dependency to KMP Project**

#### **Gradle (common `build.gradle.kts`)**

```kotlin
commonMain.dependencies {
    implementation("io.github.mohaberabi.s3klient:s3klient:0.0.1")
}
```

#### **Enable CocoaPods in `build.gradle.kts` (shared module)**

```kotlin

```

### **2️⃣ iOS Client App Setup**

Since S3Klient uses **CocoaPods AWS SDK**, the client **must also include AWS dependencies**.

#### **Initialize CocoaPods in iOS Project**

Run in the `iosApp` directory:

```sh
pod init
```

#### **Modify `iosApp/Podfile`**

Add AWS SDK dependencies to the `Podfile`:

```ruby
target 'iosApp' do
  use_frameworks!
  
  pod 'S3Klient' # Your KMP Library
  pod 'AWSS3', '2.31.0'  # ✅ Add AWS S3 SDK
  pod 'AWSCore', '2.31.0'  # ✅ Add AWS Core SDK
end
```

#### **Install Dependencies**

```sh
cd iosApp
pod install --repo-update
```

### **3️⃣ Link AWS Frameworks in Xcode**

1. Open `iosApp.xcworkspace` in **Xcode**
2. Navigate to **Build Phases** → `Link Binary With Libraries`
3. Add:
    - `AWSCore.framework`
    - `AWSS3.framework`

---

## 🚀 Usage

### **1️⃣ Create `S3Klient` Instance**

#### **Android Implementation**

```kotlin
val s3Klient: S3Klient = AndroidS3KlientFactory(
    context, S3KlientCredentials(
        cognitoIdentityId = "YOUR_COGNITO_IDENTITY_ID",
        cognitoRegion = CognitoRegion.US_EAST_1
    )
).create()
```

#### **iOS Implementation**

```kotlin
val s3Klient: S3Klient = IosS3KlientFactory(
    S3KlientCredentials(
        cognitoIdentityId = "YOUR_COGNITO_IDENTITY_ID",
        cognitoRegion = CognitoRegion.US_EAST_1
    )
).create()
```

### **2️⃣ Upload a File**

```kotlin
val result: UploadFileResult = s3Klient.uploadFile(
    AwsFileUploadRequest(
        filePath = "/path/to/your/file",
        bucket = "your-bucket-name",
        contentType = "image/png"
    )
)
```

### **3️⃣ Handle Upload Result**

```kotlin
when (result) {
    is UploadFileResult.Uploaded -> println("✅ File uploaded: ${result.path}")
    is UploadFileResult.Canceled -> println("⚠ Upload canceled: ${result.message}")
    is UploadFileResult.DidNotComplete -> println("Upload failed: ${result.message}")
    is UploadFileResult.Error -> println("Error: ${result.message}")
    UploadFileResult.Unknown -> println("Unknown status")
}
```

---

## 🎯 Architecture

### **`S3Klient` Interface**

```kotlin
interface S3Klient {
    suspend fun uploadFile(request: AwsFileUploadRequest): UploadFileResult
}
```

### **Android Implementation** (`AndroidS3Klient`)

```kotlin
class AndroidS3Klient(
    private val context: Context,
    private val credentials: S3KlientCredentials
) : S3Klient {
    override suspend fun uploadFile(request: AwsFileUploadRequest): UploadFileResult {
    }
}
```

### **iOS Implementation** (`IosS3Klient`)

```kotlin
class IosS3Klient(
    private val credentials: S3KlientCredentials
) : S3Klient {
    override suspend fun uploadFile(request: AwsFileUploadRequest): UploadFileResult {
    }
}
```

