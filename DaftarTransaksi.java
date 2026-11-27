package rental;

// Class untuk mengelola daftar transaksi dan invoice.
// Bagian anggota 3.

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

/** Mengelola semua transaksi. */
public class DaftarTransaksi {
    private final ArrayList<Transaksi> daftar = new ArrayList<>();

    /** Menambahkan transaksi ke daftar. */
    public void tambah(Transaksi t) throws TransaksiTidakValidException {
        if (t == null) {
            throw new TransaksiTidakValidException("Transaksi tidak boleh kosong.");
        }
        if (daftar.contains(t)) {
            throw new TransaksiTidakValidException("Transaksi " + t.getId() + " sudah terdaftar.");
        }
        daftar.add(t);
    }

    /** Mengambil transaksi yang masih menunggu. */
    public ArrayList<Transaksi> getMenunggu() {
        return filter(StatusTransaksi.MENUNGGU);
    }

    /** Mengambil transaksi yang sedang disewa. */
    public ArrayList<Transaksi> getDisewa() {
        return filter(StatusTransaksi.DISEWA);
    }

    /** Mencari transaksi berdasarkan ID. */
    public Transaksi cariById(String id) throws TransaksiTidakValidException {
        if (id != null) {
            for (Transaksi t : daftar) {
                if (t.getId().equalsIgnoreCase(id.trim())) {
                    return t;
                }
            }
        }
        throw new TransaksiTidakValidException("Transaksi dengan ID '" + id + "' tidak ditemukan.");
    }

    /** Mengambil semua transaksi. */
    public ArrayList<Transaksi> getSemua() {
        return new ArrayList<>(daftar);
    }

    // Memfilter transaksi berdasarkan status.
    private ArrayList<Transaksi> filter(StatusTransaksi status) {
        ArrayList<Transaksi> hasil = new ArrayList<>();
        for (Transaksi t : daftar) {
            if (t.getStatus() == status) {
                hasil.add(t);
            }
        }
        return hasil;
    }
}

/** Menampilkan invoice transaksi. */
class Invoice {
    private final Transaksi transaksi;
    private final double total;
    private final double denda;

    /** Membuat invoice dari data transaksi. */
    public Invoice(Transaksi transaksi) {
        this.transaksi = transaksi;
        this.total = transaksi.hitungTotal();
        this.denda = transaksi.hitungDenda();
    }

    /** Mengubah angka ke format Rupiah. */
    public static String formatRupiah(double nilai) {
        DecimalFormatSymbols simbol = new DecimalFormatSymbols(Locale.ROOT);
        simbol.setGroupingSeparator('.');
        simbol.setDecimalSeparator(',');
        return "Rp " + new DecimalFormat("#,##0", simbol).format(nilai);
    }

    /** Menampilkan invoice lengkap. */
    public void cetakInvoice() {
        String garis = "=".repeat(56);
        String judul = transaksi.getStatus() == StatusTransaksi.SELESAI
                ? "INVOICE PENGEMBALIAN" : "INVOICE SEWA";
        System.out.println("\n" + garis);
        System.out.println("   RENTAL BUSANA ADAT & KOSTUM - " + judul);
        System.out.println(garis);
        System.out.println("No. Transaksi : " + transaksi.getId());
        System.out.println("Penyewa       : " + transaksi.getPenyewa().getNama());
        System.out.println("Tanggal sewa  : " + transaksi.getTanggalSewa());
        System.out.println("Jatuh tempo   : " + transaksi.getTanggalJatuhTempo());
        if (transaksi.getTanggalKembali() != null) {
            System.out.println("Tgl kembali   : " + transaksi.getTanggalKembali());
        }
        System.out.println("Lama sewa     : " + transaksi.getLamaSewa() + " hari");
        System.out.println("-".repeat(56));
        System.out.printf("%-8s %-24s %-10s %12s%n", "ID", "Nama barang", "Kategori", "Biaya");
        for (Barang b : transaksi.getBarang()) {
            System.out.printf("%-8s %-24s %-10s %12s%n", b.getId(), potong(b.getNama(), 24),
                    potong(b.getKategori(), 10), formatRupiah(b.hitungBiaya(transaksi.getLamaSewa())));
        }
        System.out.println("-".repeat(56));
        System.out.printf("%-44s %12s%n", "Total sewa", formatRupiah(total));
        if (denda > 0) {
            System.out.printf("%-44s %12s%n", "Denda (" + transaksi.getHariTerlambat() + " hari x "
                    + formatRupiah(transaksi.getTarifDendaPerHari()) + ")", formatRupiah(denda));
        } else {
            System.out.printf("%-44s %12s%n", "Denda", formatRupiah(0));
        }
        System.out.printf("%-44s %12s%n", "TOTAL AKHIR", formatRupiah(total + denda));
        System.out.println("-".repeat(56));
        String ket = transaksi.getPembayaran().getKeterangan();
        System.out.println("Pembayaran    : " + transaksi.getPembayaran().getNamaMetode()
                + (ket.isEmpty() ? "" : " (" + ket + ")"));
        System.out.println("Status        : " + transaksi.getStatus().getLabel());
        System.out.println(garis);
    }

    // Memotong teks jika terlalu panjang.
    private static String potong(String teks, int maks) {
        return teks.length() <= maks ? teks : teks.substring(0, maks - 1) + ".";
    }
}