# withmo
<img src="https://github.com/user-attachments/assets/d6b26132-d4a8-446a-b2a9-5630cd85cca0" width="100%">

[![Watch the video](https://img.shields.io/badge/PV-YouTube-red?style=for-the-badge&logo=youtube&logoColor=white)](https://youtu.be/S2n97pJL9r0)
[![Slide](https://img.shields.io/badge/Slide-GoogleSlides-blue?style=for-the-badge&logo=google-slides&logoColor=white)](https://docs.google.com/presentation/d/1P8pJtk8YnazgKQcsffUqSHlIYSe43K1r/edit?slide=id.p1#slide=id.p1)
[![Download APK](https://img.shields.io/badge/Download-APK-brightgreen?style=for-the-badge&logo=android&logoColor=white)](https://drive.google.com/drive/u/2/folders/1OUCGfjTxtSnUwp8x5HOG94MYGUsn2uIL)


## withmoとは
wihtmoは、デジタルフィギュア × ランチャーをコンセプトにした、お気に入りの3Dモデルをホーム画面に表示することができるランチャーアプリです！  
withmoは、Unity as a Library を用いて作成されており、UnityプロジェクトをAndroidプロジェクトに取り込んでいるという構造になっています。本リポジトリは、そんなwithmoのAndroidリポジトリです。  
⚠注意: unityLibraryはサイズが大きく、.gitignoreしています。そのため、このリポジトリをクローンしてもアプリを起動することはできません。ビルドしたapkファイルをまとめているDriveリンクがあるのでそこからダウンロードして使用してください。また、現在クローズドテスト中です。よろしければテスターとして参加していただけると幸いです。


## UI
withmoは、時間帯に応じて背景が変化します！
| 昼 | 夕方 | 夜 |
|-------|-------|-------|
| <img src="https://github.com/user-attachments/assets/826d07b2-0978-4c1d-ad1e-c518c6e978de" width="100%" /> | <img src="https://github.com/user-attachments/assets/1383d48d-e7e9-4e8c-98b0-5598041fa0ce" width="100%" /> | <img src="https://github.com/user-attachments/assets/f80a5836-8e9b-40ee-bb86-7e1c7e862148" width="100%" /> |


ユーザーはウィジェットの配置やモデルの選択など、自由なカスタマイズが可能です
|  |  |  |
|-------|-------|-------|
| <img src="https://github.com/user-attachments/assets/25efadf6-ff98-411e-bb28-b48a54bfadb0" width="100%" /> | <img src="https://github.com/user-attachments/assets/366192b4-6887-472d-90b7-06192f2fcf22" width="100%" /> | <img src="https://github.com/user-attachments/assets/e7494ead-4fde-4c5c-b652-c1d70b2358c2" width="100%" /> |


## 使用した技術

| 項目     | 技術　    | 補足     |
|-------------|-------------|-------------|
| 言語    | Kotlin   | 型安全でシンプルな記述が可能    |
| UIフレームワーク    | Jetpack Compose     | モダンなUIフレームワーク |
| Unity    | Unity as a Library     | Unityプロジェクトとの連携 |
| デプロイ    | GitHub Pages   | GitHub Actions を活用して自動デプロイを実施     |
| CI    | GitHub Actions    | Pull Request 時に自動でコード解析    |
| 静的解析ツール    | detekt   | コードの品質維持に活用    |

## 🏆 受賞・参加実績
- 技育CAMP2023 vol14 優秀賞
- 技育博 参加
- 技育展2024 YUMEMI賞


