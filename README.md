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