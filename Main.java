package rental;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Kelas utama (entry point) untuk menjalankan aplikasi CLI Sistem Rental Busana Adat dan Kostum.
 * Mengelola daftar akun terdaftar, proses autentikasi login, perulangan menu utama, serta logout.
 *
 * @author Anggota 2
 * @version 1.0
 */
public class Main {
    private static Akun akunAktif = null;
    private static ArrayList<Akun> daftarAkun = new ArrayList<>();

    /**
     * Titik masuk utama program CLI Rental Busana Adat dan Kostum.
     *
     * @param args argumen baris perintah
     */
    public static void main(String[] args) {
        // Inisialisasi akun tetap (1 Admin dan beberapa akun Penyewa)
        // Satu Scanner dan satu objek data yang dibagikan ke semua bagian aplikasi
        Scanner scanner = new Scanner(System.in);
        DaftarBarang daftarBarang = new DaftarBarang();
        DaftarTransaksi daftarTransaksi = new DaftarTransaksi();

        daftarAkun.add(new Admin("ADM-01", "admin", "admin123", daftarBarang, daftarTransaksi, scanner));
        daftarAkun.add(new Penyewa("PNY-01", "penyewa1", "12345", "Syaula Balqea", "081234567890",
                daftarBarang, daftarTransaksi, scanner));
        daftarAkun.add(new Penyewa("PNY-02", "penyewa2", "12345", "Budi Santoso", "089876543210",
                daftarBarang, daftarTransaksi, scanner));
        boolean berjalan = true;

        while (berjalan) {
            System.out.println("\n=============================================");
            System.out.println("  SISTEM RENTAL BUSANA ADAT DAN KOSTUM");
            System.out.println("=============================================");
            System.out.println("1. Login");
            System.out.println("2. Keluar Aplikasi");
            System.out.print("Pilih menu: ");
            String pilihan = scanner.nextLine();

            if (pilihan.equals("1")) {
                try {
                    login(scanner);
                    if (akunAktif != null) {
                        akunAktif.tampilkanMenu();
                        logout();
                    }
                } catch (LoginGagalException e) {
                    System.out.println("[LOGIN GAGAL] " + e.getMessage());
                }
            } else if (pilihan.equals("2")) {
                berjalan = false;
                System.out.println("Terima kasih telah menggunakan aplikasi.");
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
        scanner.close();
    }

    /**
     * Memproses autentikasi pengguna berdasarkan username dan password yang diinput.
     * Jika berhasil, atribut {@code akunAktif} akan diisi dengan akun yang sesuai.
     *
     * @param scanner objek {@code Scanner} untuk membaca input kredensial
     * @throws LoginGagalException jika kombinasi username dan password tidak ditemukan
     */
    public static void login(Scanner scanner) throws LoginGagalException {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        for (Akun akun : daftarAkun) {
            if (akun.login(username, password)) {
                akunAktif = akun;
                System.out.println("Login berhasil! Selamat datang, " + akun.getUsername() + ".");
                return;
            }
        }
        throw new LoginGagalException("Username atau password salah. Silakan coba lagi.");
    }

    /**
     * Mengakhiri sesi pengguna yang sedang aktif dengan mereset {@code akunAktif} menjadi null.
     */
    public static void logout() {
        akunAktif = null;
    }
}
