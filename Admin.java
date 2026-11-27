package rental;

import java.util.Scanner;

/**
 * Kelas turunan dari {@code Akun} yang merepresentasikan Administrator sistem rental.
 * Bertugas mengarahkan admin ke submenu pengelolaan barang dan transaksi.
 *
 * @author Anggota 2
 * @version 1.0
 */
public class Admin extends Akun {
    private final DaftarBarang daftarBarang;
    private final DaftarTransaksi daftarTransaksi;
    private final Scanner scanner;

    /**
     * Konstruktor untuk membuat akun Admin baru.
     *
     * @param id       ID unik admin
     * @param username nama pengguna untuk login admin
     * @param password kata sandi untuk login admin
     * @param daftarBarang katalog barang bersama (Anggota 1)
     * @param daftarTransaksi pusat data transaksi bersama (Anggota 3)
     * @param scanner scanner input bersama
     */
    public Admin(String id, String username, String password,
                 DaftarBarang daftarBarang, DaftarTransaksi daftarTransaksi, Scanner scanner) {
        super(id, username, password);
        this.daftarBarang = daftarBarang;
        this.daftarTransaksi = daftarTransaksi;
        this.scanner = scanner;
    }

    /**
     * Menampilkan menu utama Admin yang menghubungkan ke submenu kelola barang
     * (milik Anggota 1) dan submenu transaksi (milik Anggota 3).
     */
    @Override
    public void tampilkanMenu() {
        int pilihan = 0;

        while (pilihan != 3) {
            System.out.println("\n=== MENU ADMIN (" + getUsername() + ") ===");
            System.out.println("1. Kelola Data Barang");
            System.out.println("2. Kelola Transaksi & Pengembalian");
            System.out.println("3. Logout");
            System.out.print("Pilih menu: ");

            try {
                pilihan = Integer.parseInt(scanner.nextLine());
                switch (pilihan) {
                    case 1:
                        new MenuBarang(daftarBarang, scanner).jalankan();
                        break;
                    case 2:
                        new MenuTransaksi(daftarTransaksi, scanner).jalankan();
                        break;
                    case 3:
                        System.out.println("Logout dari akun Admin...");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka!");
            }
        }
    }
}
