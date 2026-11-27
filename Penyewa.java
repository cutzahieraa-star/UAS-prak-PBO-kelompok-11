package rental;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Kelas turunan dari {@code Akun} yang merepresentasikan pelanggan/penyewa busana.
 * Mengelola interaksi penyewa mulai dari melihat katalog barang, mengisi keranjang sewa,
 * melakukan proses checkout transaksi, hingga melihat riwayat sewa yang disetujui admin.
 *
 * <p>Catatan integrasi: file asli terpotong pada method {@code lihatBarang()}; bagian
 * setelahnya dilengkapi sesuai pembagian tugas (menu 1-6 penyewa).</p>
 *
 * @author Anggota 2
 * @version 1.1
 */
public class Penyewa extends Akun {
    private String nama;
    private String noTelp;
    private KeranjangSewa keranjang;
    private ArrayList<Transaksi> riwayat;
    private final DaftarBarang daftarBarang;
    private final DaftarTransaksi daftarTransaksi;
    private final Scanner scanner;

    /**
     * Konstruktor untuk membuat akun Penyewa baru.
     *
     * @param id              ID unik penyewa
     * @param username        nama pengguna untuk login penyewa
     * @param password        kata sandi untuk login penyewa
     * @param nama            nama lengkap penyewa
     * @param noTelp          nomor telepon aktif penyewa
     * @param daftarBarang    katalog barang bersama (milik Anggota 1)
     * @param daftarTransaksi pusat data transaksi bersama (milik Anggota 3)
     * @param scanner         scanner input yang dipakai bersama seluruh aplikasi
     */
    public Penyewa(String id, String username, String password, String nama, String noTelp,
                   DaftarBarang daftarBarang, DaftarTransaksi daftarTransaksi, Scanner scanner) {
        super(id, username, password);
        this.nama = nama;
        this.noTelp = noTelp;
        this.keranjang = new KeranjangSewa();
        this.riwayat = new ArrayList<>();
        this.daftarBarang = daftarBarang;
        this.daftarTransaksi = daftarTransaksi;
        this.scanner = scanner;
    }

    /**
     * Mengambil nama lengkap penyewa.
     *
     * @return nama penyewa
     */
    public String getNama() {
        return nama;
    }

    /**
     * Mengambil nomor telepon penyewa.
     *
     * @return nomor telepon penyewa
     */
    public String getNoTelp() {
        return noTelp;
    }

    /**
     * Menampilkan daftar seluruh barang yang sedang tersedia untuk disewa.
     */
    public void lihatBarang() {
        ArrayList<Barang> tersedia = daftarBarang.getBarangTersedia();
        if (tersedia.isEmpty()) {
            System.out.println("Saat ini tidak ada barang yang tersedia.");
            return;
        }
        System.out.println("\n=============== BARANG TERSEDIA ===============");
        for (Barang b : tersedia) {
            System.out.println(b);
        }
    }

    /**
     * Memproses checkout: meminta lama sewa dan metode pembayaran, lalu membuat
     * {@code Transaksi} milik Anggota 3 dan mendaftarkannya ke {@code DaftarTransaksi}.
     * Transaksi berstatus menunggu sampai diterima admin.
     *
     * @throws KeranjangKosongException jika keranjang masih kosong
     */
    public void buatTransaksi() throws KeranjangKosongException {
        if (keranjang.getDaftarBarang().isEmpty()) {
            throw new KeranjangKosongException("Keranjang masih kosong. Tambahkan barang terlebih dahulu.");
        }
        keranjang.lihatIsi();

        int lamaSewa;
        System.out.print("Lama sewa (hari): ");
        try {
            lamaSewa = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Lama sewa harus berupa angka.");
            return;
        }

        System.out.println("Pilih metode pembayaran:");
        System.out.println("1. Transfer Bank");
        System.out.println("2. QRIS");
        System.out.println("3. Tunai");
        System.out.print("Pilih: ");
        Pembayaran pembayaran;
        switch (scanner.nextLine().trim()) {
            case "1": pembayaran = new TransferBank(scanner); break;
            case "2": pembayaran = new QRIS(); break;
            case "3": pembayaran = new Tunai(scanner); break;
            default:
                System.out.println("Metode pembayaran tidak valid. Checkout dibatalkan.");
                return;
        }

        try {
            Transaksi t = new Transaksi(this, keranjang.getDaftarBarang(), lamaSewa, pembayaran);
            daftarTransaksi.tambah(t);
            keranjang.kosongkan();
            System.out.println("\nCheckout berhasil! No. transaksi: " + t.getId()
                    + " | Total: " + Invoice.formatRupiah(t.hitungTotal()));
            System.out.println("Status: menunggu persetujuan admin.");
        } catch (TransaksiTidakValidException | PembayaranGagalException e) {
            System.out.println("[CHECKOUT GAGAL] " + e.getMessage());
        }
    }

    /**
     * Menampilkan riwayat sewa. Hanya transaksi yang sudah diterima admin yang tercatat.
     */
    public void lihatRiwayat() {
        if (riwayat.isEmpty()) {
            System.out.println("Belum ada riwayat sewa (hanya transaksi yang sudah diterima admin).");
            return;
        }
        System.out.println("\n--- Riwayat Sewa ---");
        for (Transaksi t : riwayat) {
            System.out.println(t);
        }
    }

    /**
     * Menambahkan transaksi yang sudah diterima admin ke riwayat penyewa.
     * Dipanggil oleh {@code Transaksi.terima()}.
     *
     * @param t transaksi yang diterima
     */
    public void tambahRiwayat(Transaksi t) {
        riwayat.add(t);
    }

    /**
     * Menampilkan menu Penyewa (1-6) dan menjalankannya sampai penyewa logout.
     */
    @Override
    public void tampilkanMenu() {
        int pilihan = 0;
        while (pilihan != 6) {
            System.out.println("\n=== MENU PENYEWA (" + nama + ") ===");
            System.out.println("1. Lihat barang tersedia");
            System.out.println("2. Tambah ke keranjang");
            System.out.println("3. Lihat / hapus keranjang");
            System.out.println("4. Checkout");
            System.out.println("5. Riwayat sewa");
            System.out.println("6. Logout");
            System.out.print("Pilih menu: ");
            try {
                pilihan = Integer.parseInt(scanner.nextLine().trim());
                switch (pilihan) {
                    case 1: lihatBarang(); break;
                    case 2:
                        System.out.print("ID barang: ");
                        try {
                            keranjang.tambahBarang(daftarBarang.cariById(scanner.nextLine().trim()));
                        } catch (BarangTidakDitemukanException e) {
                            System.out.println("Gagal: " + e.getMessage());
                        }
                        break;
                    case 3:
                        keranjang.lihatIsi();
                        if (!keranjang.getDaftarBarang().isEmpty()) {
                            System.out.print("ID barang yang dihapus (kosong = kembali): ");
                            String id = scanner.nextLine().trim();
                            if (!id.isEmpty()) keranjang.hapusBarang(id);
                        }
                        break;
                    case 4:
                        try {
                            buatTransaksi();
                        } catch (KeranjangKosongException e) {
                            System.out.println("[CHECKOUT GAGAL] " + e.getMessage());
                        }
                        break;
                    case 5: lihatRiwayat(); break;
                    case 6: System.out.println("Logout dari akun Penyewa..."); break;
                    default: System.out.println("Pilihan tidak valid.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka!");
            }
        }
    }
}
