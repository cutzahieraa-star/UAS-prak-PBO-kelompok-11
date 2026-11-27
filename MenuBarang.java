package rental;

import java.util.Scanner;

public class MenuBarang {
    private final DaftarBarang daftarBarang;
    private final Scanner scanner;

    public MenuBarang(DaftarBarang daftarBarang, Scanner scanner) {
        if (daftarBarang == null || scanner == null) {
            throw new IllegalArgumentException("Daftar barang dan scanner tidak boleh null.");
        }
        this.daftarBarang = daftarBarang;
        this.scanner = scanner;
    }

    public void jalankan() {
        boolean kembali = false;
        while (!kembali) {
            System.out.println("\n========== MENU KELOLA BARANG ==========");
            System.out.println("1. Lihat semua barang");
            System.out.println("2. Tambah barang");
            System.out.println("3. Edit barang");
            System.out.println("4. Hapus barang");
            System.out.println("0. Kembali");
            System.out.print("Pilih menu: ");
            String pilihan = scanner.nextLine().trim();

            try {
                switch (pilihan) {
                    case "1": daftarBarang.tampilkanSemua(); break;
                    case "2": tambahBarang(); break;
                    case "3": editBarang(); break;
                    case "4": hapusBarang(); break;
                    case "0": kembali = true; break;
                    default: System.out.println("Pilihan tidak valid.");
                }
            } catch (BarangTidakDitemukanException | BarangSedangDisewaException
                     | IllegalArgumentException e) {
                System.out.println("Gagal: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan input: " + e.getMessage());
            }
        }
    }

    private void tambahBarang() {
        System.out.println("\nPilih kategori:");
        System.out.println("1. Baju Adat");
        System.out.println("2. Kostum Karakter/Cosplay");
        System.out.println("3. Busana Pengantin/Pesta");
        System.out.print("Kategori: ");
        String kategori = scanner.nextLine().trim();

        System.out.print("ID barang (contoh BA-004): ");
        String id = scanner.nextLine();
        System.out.print("Nama barang: ");
        String nama = scanner.nextLine();
        System.out.print("Ukuran: ");
        String ukuran = scanner.nextLine();
        double tarif = bacaDouble("Tarif harian: ");

        Barang barang;
        switch (kategori) {
            case "1":
                System.out.print("Asal daerah: ");
                barang = new BajuAdat(id, nama, ukuran, tarif, scanner.nextLine());
                break;
            case "2":
                System.out.print("Asal karakter: ");
                String karakter = scanner.nextLine();
                double perawatan = bacaDouble("Biaya perawatan/cuci: ");
                barang = new KostumKarakter(id, nama, ukuran, tarif, karakter, perawatan);
                break;
            case "3":
                double fitting = bacaDouble("Biaya jasa fitting/rias: ");
                barang = new BusanaPengantin(id, nama, ukuran, tarif, fitting);
                break;
            default:
                System.out.println("Kategori tidak valid.");
                return;
        }

        daftarBarang.tambahBarang(barang);
        System.out.println("Barang berhasil ditambahkan.");
    }

    private void editBarang() throws BarangTidakDitemukanException, BarangSedangDisewaException {
        System.out.print("Masukkan ID barang yang ingin diedit: ");
        String id = scanner.nextLine();
        Barang barang = daftarBarang.cariById(id);

        System.out.println("Data saat ini: " + barang);
        System.out.print("Nama baru: ");
        String nama = scanner.nextLine();
        System.out.print("Ukuran baru: ");
        String ukuran = scanner.nextLine();
        double tarif = bacaDouble("Tarif harian baru: ");

        daftarBarang.editBarang(id, nama, ukuran, tarif);
        System.out.println("Data barang berhasil diperbarui.");
    }

    private void hapusBarang() throws BarangTidakDitemukanException, BarangSedangDisewaException {
        System.out.print("Masukkan ID barang yang ingin dihapus: ");
        String id = scanner.nextLine();
        daftarBarang.hapusBarang(id);
        System.out.println("Barang berhasil dihapus.");
    }

    private double bacaDouble(String pesan) {
        while (true) {
            System.out.print(pesan);
            String input = scanner.nextLine().trim().replace(",", ".");
            try {
                double nilai = Double.parseDouble(input);
                if (nilai < 0) {
                    System.out.println("Angka tidak boleh negatif.");
                    continue;
                }
                return nilai;
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka yang valid.");
            }
        }
    }
}
