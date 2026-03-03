Refleksi 1: Clean Code dan Secure Coding

1. Penerapan Clean Code

Dalam mengimplementasikan fitur Edit dan Delete, saya telah menerapkan beberapa prinsip Clean Code:
-Meaningful Names: Saya menggunakan nama fungsi yang deskriptif (contoh: findById, deleteProduct) sehingga tujuan kode langsung dapat dipahami tanpa komentar berlebih.
-Single Responsibility Principle (SRP): Saya memisahkan tanggung jawab dengan jelas. ProductController hanya mengatur request HTTP, ProductService menangani logika bisnis (seperti pembuatan ID), dan ProductRepository khusus menangani penyimpanan data.

2. Penerapan Secure Coding

Saya menggunakan UUID untuk ID produk untuk membuat ID unik secara global dan sulit ditebak, sehingga mencegah Enumeration Attacks.

3. Kekurangan dan Perbaikan Kode

Setelah meninjau kembali kode saya, saya menemukan satu kesalahan desain keamanan pada fitur Delete. Saat ini tombol Delete diimplementasikan menggunakan method GET (@GetMapping).
Hal ini menyalahi standar RESTful dan rentan terhadap serangan CSRF (Cross-Site Request Forgery). Penyerang bisa membuat link jebakan yang jika diklik user akan menghapus data tanpa persetujuan.
Seharusnya fitur Delete menggunakan method POST atau DELETE. Di sisi HTML (Thymeleaf), tombol delete sebaiknya dibungkus dalam <form> dengan method="post", bukan sekadar menggunakan tag <a>.


Refleksi 2: Unit Testing & Functional Test Cleanliness

1. Perasaan & Evaluasi Unit Test

Setelah menulis unit test, saya merasa lebih percaya diri dengan kualitas kode yang saya tulis. Unit test bertindak sebagai jaring pengaman yang memastikan bahwa perubahan kode di masa depan (refactoring) tidak merusak fitur yang sudah berjalan (regression).

Berapa banyak unit test yang harus dibuat? 
Tidak ada angka pasti, jumlahnya bergantung pada kompleksitas method. Prinsipnya, setiap jalur logika (execution path) harus diuji. Ini mencakup skenario positif, negatif, dan edge cases (batas nilai).

Bagaimana memastikan unit test cukup? 
Kita bisa menggunakan metrik Code Coverage. Ini membantu memvisualisasikan baris kode mana yang sudah dieksekusi oleh test dan mana yang belum.

Apakah 100% Code Coverage berarti bebas bug? 
Tidak. 100% coverage hanya berarti setiap baris kode telah dijalankan setidaknya sekali saat testing. Itu tidak menjamin kebenaran logika bisnis.
Contoh: Sebuah fungsi pengurangan a - b mungkin salah ditulis menjadi a + b. Test bisa saja pass (ter-cover) jika inputnya 0 dan 0, tapi logikanya tetap salah.

2. Kebersihan Kode dalam Functional Test

Jika saya membuat functional test baru untuk menghitung jumlah item dengan cara copy-paste setup prosedur dan variabel instance dari CreateProductFunctionalTest.java, itu akan menurunkan kualitas kode.

Masalah Kebersihan Kode (Code Cleanliness Issue): Tindakan tersebut melanggar prinsip DRY (Don't Repeat Yourself). Akan terjadi Code Duplication pada bagian setup konfigurasi (seperti inisialisasi port).

Alasan Mengapa Buruk: Duplikasi membuat kode sulit di maintain. Jika suatu saat saya perlu mengubah cara setup driver atau port, saya harus mengubahnya di semua file test satu per satu.

Saran Perbaikan (Improvement): Solusinya adalah menggunakan prinsip Inheritance (Pewarisan) dalam OOP.
Buat satu Base Class (misalnya BaseFunctionalTest) yang menangani semua konfigurasi umum (@LocalServerPort, setup URL, inisialisasi WebDriver).
Test class lain (seperti CreateProductFunctionalTest atau CountProductFunctionalTest) cukup meng-extends Base Class tersebut.
Dengan cara ini, kode setup hanya ditulis satu kali dan bisa digunakan ulang dengan bersih.



Module 2 Reflection

1. Code Quality Issues Fixed

Selama mengerjakan exercise, saya memperbaiki beberapa masalah kualitas kode yang terdeteksi oleh PMD:

- Modifier `public` yang tidak perlu pada method di interface `ProductService.java`: 
Method yang dideklarasikan di Java interface secara otomatis bersifat `public abstract`. PMD menandai kelima method (`create`, `findAll`, `findById`, `update`, `delete`) karena memiliki modifier `public` yang redundan. Strategi saya cukup sederhana — saya menghapus keyword `public` dari setiap deklarasi method di interface tersebut, sehingga 5 warning sekaligus terselesaikan tanpa mengubah perilaku kode.

- Meningkatkan test coverage hingga 100%. Menambahkan unit test untuk bagian kode yang sebelumnya belum ter-cover, termasuk test controller untuk `HomeController` dan `ProductController` menggunakan mock Mockito, test untuk `EshopApplication.main()`, serta test tambahan di service yang meng-cover branch pembuatan UUID (product ID null), branch update ketika produk tidak ditemukan, dan branch iterasi `findById` ketika `equals` mengembalikan false.

2. Refleksi CI/CD Workflow

Ya, saya percaya implementasi saat ini sudah memenuhi definisi Continuous Integration dan Continuous Deployment. Untuk Continuous Integration, setiap push dan pull request secara otomatis menjalankan test suite dan analisis kualitas kode (PMD), sehingga perubahan kode divalidasi sebelum di-merge. Ini berarti kode yang rusak atau regresi kualitas dapat terdeteksi lebih awal secara otomatis, tanpa intervensi manual. Untuk Continuous Deployment, workflow sudah mencakup deployment otomatis ke PaaS (Koyeb) setelah merge, yang berarti setelah kode lolos semua pengecekan dan di-merge, kode tersebut langsung dikirim ke production tanpa memerlukan langkah deployment manual terpisah. Secara keseluruhan, workflow ini membentuk pipeline CI/CD yang lengkap, mencakup building, testing, analyzing, dan deploying aplikasi secara otomatis.


Refleksi 3: Penerapan SOLID (Singkat)

1) Prinsip yang diterapkan
- SRP: Controller hanya urus HTTP, service urus bisnis, repository urus persistence dan CarController dipisah dari ProductController.
- DIP: Service bergantung pada abstraksi `ProductRepository` dan di-inject lewat konstruktor sedangkan implementasi konkret dipisah ke `InMemoryProductRepository`.
- ISP: Kontrak repository didefinisikan minimal (create/findById/update/delete) sesuai kebutuhan domain produk.

2) Keuntungan menerapkan SOLID (contoh)
- Mudah diganti: `ProductRepository` bisa diganti implementasi database tanpa ubah service/controller.
- Lebih teruji: Konstruktor injection memudahkan mocking di test (contoh ProductServiceImplTest), sehingga regresi cepat terdeteksi.
- Tanggung jawab jelas: Pemisahan CarController mencegah product flow tercampur dengan car flow, membuat perubahan fitur lebih lokal.

3) Kerugian jika SOLID diabaikan (contoh)
- Ketergantungan kaku: Jika service langsung memakai list internal, pindah ke database akan memerlukan ubahan besar di banyak kelas.
- Sulit diuji: Tanpa abstraksi repository, unit test harus memanipulasi state global atau memakai real storage sehingga lambat/rapuh.
- Efek samping silang: Menggabungkan controller (seperti sebelumnya CarController mewarisi ProductController) bisa membuat routing dan logika saling memengaruhi, meningkatkan risiko bug.