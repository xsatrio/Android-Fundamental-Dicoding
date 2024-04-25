# Submission Akhir Android Developer Dicoding Path 3

Saya harap proyek ini bisa dijadikan referensi bagi para pembaca, dan jangan menggunakan proyek ini jika hanya ingin copy paste untuk submission, karena akan terdeteksi otomatis sebagai plagiarisme.

## Instalasi

- Generate API Token akun github.
- Masukkan API Token ke build.gradle.kts(module :app) pada buildTypes :
```bash
     buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "GITHUB_API_KEY", "\"apikey\"")
            buildConfigField("String", "BASE_URL", "\"https://api.github.com/\"")
        }
        debug {
            buildConfigField("String", "GITHUB_API_KEY", "\"apikey\"")
            buildConfigField("String", "BASE_URL", "\"https://api.github.com/\"")
        }
    }
``` 
- ganti "apikey" dengan Token yang sudah di buat.

## Screenshot

<img src="https://raw.githubusercontent.com/xsatrio/submission-android-developer-dicoding-path-3/main/Preview/Screenshot_2024-04-25-14-00-01-356_com.dicoding.githubuser-edit.png"
     alt="Splash Screen"
     style="float: left; margin-right: 10px;"
     width="200" />
<img src="https://raw.githubusercontent.com/xsatrio/submission-android-developer-dicoding-path-3/main/Preview/Screenshot_2024-04-25-13-57-32-253_com.dicoding.githubuser-edit.png"
     alt="Light Theme"
     style="float: left; margin-right: 10px;"
     width="200" />
<img src="https://raw.githubusercontent.com/xsatrio/submission-android-developer-dicoding-path-3/main/Preview/Screenshot_2024-04-25-13-57-37-288_com.dicoding.githubuser-edit.png"
     alt="Dark Theme"
     style="float: left; margin-right: 10px;"
     width="200" />
<img src="https://raw.githubusercontent.com/xsatrio/submission-android-developer-dicoding-path-3/main/Preview/Screenshot_2024-04-25-13-57-48-770_com.dicoding.githubuser-edit.png"
     alt="Profile"
     style="float: left; margin-right: 10px;"
     width="200" />
<img src="https://raw.githubusercontent.com/xsatrio/submission-android-developer-dicoding-path-3/main/Preview/Screenshot_2024-04-25-13-57-42-946_com.dicoding.githubuser-edit.png"
     alt="Favorite List"
     style="float: left; margin-right: 10px;"
     width="200" />
<img src="https://github.com/xsatrio/submission-android-developer-dicoding-path-3/blob/main/Preview/Screenshot_2024-04-25-13-57-57-928_com.dicoding.githubuser-edit.png"
     alt="Search View"
     style="float: left; margin-right: 10px;"
     width="200" />
