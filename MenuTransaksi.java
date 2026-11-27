package rental;

// MenuTransaksi.java : menu Admin untuk transaksi

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

// Menu Admin untuk mengelola transaksi, menerima transaksi,
// melihat transaksi yang sedang disewa, dan mengonfirmasi pengembalian.
public class MenuTransaksi {
    private final DaftarTransaksi daftarTransaksi;
    private final Scanner input;

    // Membuat menu dengan Scanner baru.
    public MenuTransaksi(DaftarTransaksi daftarTransaksi) {
        this(daftarTransaksi, new Scanner(System.in));
    }

    // Membuat menu menggunakan Scanner yang sudah tersedia.
    public MenuTransaksi(DaftarTransaksi daftarTransaksi, Scanner input) {
        this.daftarTransaksi = daftarTransaksi;
        this.input = input;
    }

    // Menjalankan menu transaksi sampai admin memilih kembali.
    public void jalankan() {
        int pilihan = -1;
        while (pilihan != 0) {
            System.out.println("\n=== MENU TRANSAKSI (ADMIN) ===");
            System.out.println("1. Lihat transaksi menunggu");
            System.out.println("2. Terima transaksi");
            System.out.println("3. Lihat transaksi sedang disewa");
            System.out.println("4. Konfirmasi pengembalian");
            System.out.println("0. Kembali");
            System.out.print("Pilih: ");
            try {
                pilihan = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka.");
                pilihan = -1;
                continue;
            }
            switch (pilihan) {
                case 1 -> tampilkan("TRANSAKSI MENUNGGU", daftarTransaksi.getMenunggu());
                case 2 -> terimaTransaksi();
                case 3 -> tampilkan("TRANSAKSI SEDANG DISEWA", daftarTransaksi.getDisewa());
                case 4 -> konfirmasiPengembalian();
                case 0 -> System.out.println("Kembali ke menu Admin.");
                default -> System.out.println("Pilihan tidak tersedia.");
            }
        }
    }

    // Menampilkan daftar transaksi.
    private void tampilkan(String judul, ArrayList<Transaksi> list) {
        System.out.println("\n--- " + judul + " ---");
        if (list.isEmpty()) {
            System.out.println("(tidak ada)");
            return;
        }
        for (Transaksi t : list) {
            System.out.println(t);
        }
    }

    // Menerima transaksi dan mencetak invoice.
    private void terimaTransaksi() {
        ArrayList<Transaksi> menunggu = daftarTransaksi.getMenunggu();
        tampilkan("TRANSAKSI MENUNGGU", menunggu);
        if (menunggu.isEmpty()) {
            return;
        }
        System.out.print("ID transaksi yang diterima: ");
        try {
            Transaksi t = daftarTransaksi.cariById(input.nextLine());
            t.terima();
            System.out.println("Transaksi " + t.getId() + " diterima. Barang kini berstatus disewa.");
            new Invoice(t).cetakInvoice();
        } catch (TransaksiTidakValidException e) {
            System.out.println("Gagal: " + e.getMessage());
        }
    }

    // Mengonfirmasi pengembalian barang dan menghitung denda.
    private void konfirmasiPengembalian() {
        ArrayList<Transaksi> disewa = daftarTransaksi.getDisewa();
        tampilkan("TRANSAKSI SEDANG DISEWA", disewa);
        if (disewa.isEmpty()) {
            return;
        }
        System.out.print("ID transaksi yang dikembalikan: ");
        try {
            Transaksi t = daftarTransaksi.cariById(input.nextLine());
            System.out.print("Tanggal kembali (yyyy-MM-dd, kosong = hari ini): ");
            String teks = input.nextLine().trim();
            LocalDate tanggal = teks.isEmpty() ? LocalDate.now() : LocalDate.parse(teks);
            t.kembalikan(tanggal);
            System.out.println("Pengembalian " + t.getId() + " dicatat. Barang tersedia kembali.");
            if (t.getHariTerlambat() > 0) {
                System.out.println("Terlambat " + t.getHariTerlambat() + " hari, denda "
                        + Invoice.formatRupiah(t.hitungDenda()) + ".");
            } else {
                System.out.println("Tepat waktu, denda Rp 0.");
            }
            new Invoice(t).cetakInvoice();
        } catch (DateTimeParseException e) {
            System.out.println("Gagal: format tanggal harus yyyy-MM-dd, contoh 2026-11-28.");
        } catch (TransaksiTidakValidException e) {
            System.out.println("Gagal: " + e.getMessage());
        }
    }
}
