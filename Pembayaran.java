package rental;

// Pembayaran.java : interface Pembayaran + TransferBank, QRIS, Tunai

import java.util.Scanner;
import java.util.UUID;

// Interface untuk metode pembayaran seperti Transfer Bank, QRIS, dan Tunai.
public interface Pembayaran {

    // Memproses pembayaran sesuai jumlah yang harus dibayar.
    void prosesPembayaran(double jumlah) throws PembayaranGagalException;

    // Mengambil nama metode pembayaran.
    String getNamaMetode();

    // Mengambil keterangan tambahan dari pembayaran.
    default String getKeterangan() {
        return "";
    }
}

// Class untuk pembayaran melalui transfer bank.
class TransferBank implements Pembayaran {
    private final Scanner input;
    private String namaBank = "";
    private String noRekening = "";

    // Membuat pembayaran transfer menggunakan input dari keyboard.
    public TransferBank() {
        this(new Scanner(System.in));
    }

    // Membuat pembayaran transfer dengan Scanner yang tersedia.
    public TransferBank(Scanner input) {
        this.input = input;
    }

    // Meminta nama bank dan nomor rekening, lalu memeriksa datanya.
    @Override
    public void prosesPembayaran(double jumlah) throws PembayaranGagalException {
        System.out.println("\n[Transfer Bank] Total: " + Invoice.formatRupiah(jumlah));
        System.out.print("Nama bank        : ");
        String bank = input.nextLine().trim();
        System.out.print("Nomor rekening   : ");
        String rek = input.nextLine().trim();

        if (bank.isEmpty() || rek.isEmpty()) {
            throw new PembayaranGagalException("Nama bank dan nomor rekening tidak boleh kosong.");
        }
        if (!rek.matches("\\d{8,20}")) {
            throw new PembayaranGagalException("Nomor rekening harus berupa angka 8-20 digit.");
        }
        this.namaBank = bank;
        this.noRekening = rek;
        System.out.println("Transfer dicatat. Menunggu verifikasi admin.");
    }

    // Mengambil nama metode pembayaran.
    @Override
    public String getNamaMetode() {
        return "Transfer Bank";
    }

    // Mengambil nama bank dan nomor rekening.
    @Override
    public String getKeterangan() {
        return namaBank.isEmpty() ? "" : namaBank + " - " + noRekening;
    }
}

// Class untuk pembayaran menggunakan QRIS.
class QRIS implements Pembayaran {
    private String kodeReferensi = "";

    // Membuat kode referensi QRIS secara otomatis.
    @Override
    public void prosesPembayaran(double jumlah) throws PembayaranGagalException {
        if (jumlah <= 0) {
            throw new PembayaranGagalException("Jumlah pembayaran QRIS tidak valid.");
        }
        this.kodeReferensi = "QRIS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        System.out.println("\n[QRIS] Total: " + Invoice.formatRupiah(jumlah));
        System.out.println("Kode referensi pembayaran: " + kodeReferensi);
        System.out.println("(Simulasi) Pembayaran QRIS berhasil.");
    }

    // Mengambil nama metode pembayaran.
    @Override
    public String getNamaMetode() {
        return "QRIS";
    }

    // Mengambil kode referensi pembayaran.
    @Override
    public String getKeterangan() {
        return kodeReferensi.isEmpty() ? "" : "Ref: " + kodeReferensi;
    }
}

// Class untuk pembayaran menggunakan uang tunai.
class Tunai implements Pembayaran {
    private final Scanner input;
    private double uangDiterima;
    private double kembalian;
    private boolean sudahBayar = false;

    // Membuat pembayaran tunai menggunakan input dari keyboard.
    public Tunai() {
        this(new Scanner(System.in));
    }

    // Membuat pembayaran tunai dengan Scanner yang tersedia.
    public Tunai(Scanner input) {
        this.input = input;
    }

    // Meminta jumlah uang yang dibayarkan dan menghitung kembalian.
    @Override
    public void prosesPembayaran(double jumlah) throws PembayaranGagalException {
        System.out.println("\n[Tunai] Total: " + Invoice.formatRupiah(jumlah));
        System.out.print("Uang diterima (Rp): ");
        String teks = input.nextLine().trim();

        double uang;
        try {
            uang = Double.parseDouble(teks);
        } catch (NumberFormatException e) {
            throw new PembayaranGagalException("Uang diterima harus berupa angka.");
        }
        if (uang <= 0) {
            throw new PembayaranGagalException("Uang diterima harus lebih dari 0.");
        }
        if (uang < jumlah) {
            throw new PembayaranGagalException("Uang kurang " + Invoice.formatRupiah(jumlah - uang) + ".");
        }
        this.uangDiterima = uang;
        this.kembalian = uang - jumlah;
        this.sudahBayar = true;
        System.out.println("Kembalian: " + Invoice.formatRupiah(kembalian));
    }

    // Mengambil nama metode pembayaran.
    @Override
    public String getNamaMetode() {
        return "Tunai";
    }

    // Mengambil jumlah kembalian.
    public double getKembalian() {
        return kembalian;
    }

    // Mengambil informasi uang yang diterima dan kembalian.
    @Override
    public String getKeterangan() {
        return sudahBayar
                ? "Diterima " + Invoice.formatRupiah(uangDiterima) + ", kembalian " + Invoice.formatRupiah(kembalian)
                : "";
    }
}
