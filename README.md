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