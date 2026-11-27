package rental;

// Class transaksi dan statusnya.
// Bagian transaksi dan pembayaran.

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

// Satu transaksi sewa milik seorang penyewa.
public class Transaksi {

    // Denda per hari = 10% dari jumlah tarif harian seluruh barang dalam transaksi.
    public static final double PERSEN_DENDA_PER_HARI = 0.10;

    private static int counter = 0;

    private final String id;
    private final Penyewa penyewa;
    private final ArrayList<Barang> barang;
    private final int lamaSewa;
    private LocalDate tanggalSewa;
    private LocalDate tanggalJatuhTempo;
    private LocalDate tanggalKembali;
    private StatusTransaksi status;
    private final Pembayaran pembayaran;

    // Membuat transaksi baru (dipanggil saat checkout penyewa).
    public Transaksi(Penyewa penyewa, List<Barang> barang, int lamaSewa, Pembayaran pembayaran)
            throws TransaksiTidakValidException, PembayaranGagalException {
        if (penyewa == null) {
            throw new TransaksiTidakValidException("Penyewa tidak boleh kosong.");
        }
        if (barang == null || barang.isEmpty()) {
            throw new TransaksiTidakValidException("Transaksi harus berisi minimal 1 barang.");
        }
        if (lamaSewa <= 0) {
            throw new TransaksiTidakValidException("Lama sewa harus minimal 1 hari.");
        }
        if (pembayaran == null) {
            throw new TransaksiTidakValidException("Metode pembayaran belum dipilih.");
        }
        ArrayList<String> idTerpakai = new ArrayList<>();
        for (Barang b : barang) {
            if (idTerpakai.contains(b.getId())) {
                throw new TransaksiTidakValidException("Barang " + b.getId() + " dimasukkan lebih dari sekali.");
            }
            if (!b.isTersedia()) {
                throw new TransaksiTidakValidException(
                        "Barang " + b.getId() + " (" + b.getNama() + ") sedang disewa.");
            }
            idTerpakai.add(b.getId());
        }

        this.penyewa = penyewa;
        this.barang = new ArrayList<>(barang);
        this.lamaSewa = lamaSewa;
        this.pembayaran = pembayaran;
        this.tanggalSewa = LocalDate.now();
        this.tanggalJatuhTempo = tanggalSewa.plusDays(lamaSewa);
        this.status = StatusTransaksi.MENUNGGU;

        // Proses pembayaran saat checkout.
        pembayaran.prosesPembayaran(hitungTotal());
        counter++;
        this.id = String.format("TR-%03d", counter);
    }

    // Menghitung total biaya sewa.
    public final double hitungTotal() {
        double total = 0;
        for (Barang b : barang) {
            total += b.hitungBiaya(lamaSewa);
        }
        return total;
    }

    // Menghitung tarif denda per hari untuk transaksi ini.
    public double getTarifDendaPerHari() {
        double tarif = 0;
        for (Barang b : barang) {
            tarif += b.getTarifHarian();
        }
        return tarif * PERSEN_DENDA_PER_HARI;
    }

    // Menghitung jumlah hari keterlambatan.
    public long getHariTerlambat() {
        LocalDate acuan;
        if (tanggalKembali != null) {
            acuan = tanggalKembali;
        } else if (status == StatusTransaksi.DISEWA) {
            acuan = LocalDate.now();
        } else {
            return 0;
        }
        long selisih = ChronoUnit.DAYS.between(tanggalJatuhTempo, acuan);
        return Math.max(0, selisih);
    }

    // Menghitung denda keterlambatan = hari terlambat x tarif denda per hari.
    public double hitungDenda() {
        return getHariTerlambat() * getTarifDendaPerHari();
    }

    // Mengubah status transaksi.
    public void ubahStatus(StatusTransaksi baru) throws TransaksiTidakValidException {
        boolean boleh = (status == StatusTransaksi.MENUNGGU && baru == StatusTransaksi.DISEWA)
                || (status == StatusTransaksi.DISEWA && baru == StatusTransaksi.SELESAI);
        if (!boleh) {
            throw new TransaksiTidakValidException(
                    "Status tidak bisa diubah dari " + status.getLabel() + " ke " + baru.getLabel() + ".");
        }
        this.status = baru;
    }

    // Menerima transaksi (aksi admin).
    public void terima() throws TransaksiTidakValidException {
        if (status != StatusTransaksi.MENUNGGU) {
            throw new TransaksiTidakValidException(
                    "Transaksi " + id + " berstatus " + status.getLabel() + ", tidak bisa diterima.");
        }
        for (Barang b : barang) {
            if (!b.isTersedia()) {
                throw new TransaksiTidakValidException(
                        "Barang " + b.getId() + " (" + b.getNama() + ") sudah disewa transaksi lain.");
            }
        }
        for (Barang b : barang) {
            b.setTersedia(false);
        }
        this.tanggalSewa = LocalDate.now();
        this.tanggalJatuhTempo = tanggalSewa.plusDays(lamaSewa);
        ubahStatus(StatusTransaksi.DISEWA);
        penyewa.tambahRiwayat(this);
    }

    // Mengonfirmasi pengembalian barang (aksi admin).
    public void kembalikan(LocalDate tanggal) throws TransaksiTidakValidException {
        if (status != StatusTransaksi.DISEWA) {
            throw new TransaksiTidakValidException(
                    "Transaksi " + id + " berstatus " + status.getLabel() + ", tidak bisa dikembalikan.");
        }
        if (tanggal == null) {
            throw new TransaksiTidakValidException("Tanggal kembali tidak boleh kosong.");
        }
        if (tanggal.isBefore(tanggalSewa)) {
            throw new TransaksiTidakValidException(
                    "Tanggal kembali tidak boleh sebelum tanggal sewa (" + tanggalSewa + ").");
        }
        this.tanggalKembali = tanggal;
        for (Barang b : barang) {
            b.setTersedia(true);
        }
        ubahStatus(StatusTransaksi.SELESAI);
    }

    // Mengambil ID transaksi.
    public String getId() {
        return id;
    }

    // Mengambil penyewa pemilik transaksi.
    public Penyewa getPenyewa() {
        return penyewa;
    }

    // Mengambil salinan daftar barang pada transaksi.
    public ArrayList<Barang> getBarang() {
        return new ArrayList<>(barang);
    }

    // Mengambil lama sewa.
    public int getLamaSewa() {
        return lamaSewa;
    }

    // Mengambil tanggal mulai sewa.
    public LocalDate getTanggalSewa() {
        return tanggalSewa;
    }

    // Mengambil tanggal jatuh tempo pengembalian.
    public LocalDate getTanggalJatuhTempo() {
        return tanggalJatuhTempo;
    }

    // Mengambil tanggal barang dikembalikan.
    public LocalDate getTanggalKembali() {
        return tanggalKembali;
    }

    // Mengambil status transaksi.
    public StatusTransaksi getStatus() {
        return status;
    }

    // Mengambil metode pembayaran transaksi.
    public Pembayaran getPembayaran() {
        return pembayaran;
    }

    // Ringkasan satu baris untuk ditampilkan di daftar transaksi admin.
    @Override
    public String toString() {
        return String.format("%s | %s | %d barang | %d hari | %s | %s",
                id, penyewa.getNama(), barang.size(), lamaSewa,
                Invoice.formatRupiah(hitungTotal()), status.getLabel());
    }
}

// Status sebuah transaksi sewa.
enum StatusTransaksi {
    // Transaksi sudah dibuat penyewa, menunggu diterima admin.
    MENUNGGU("Menunggu"),
    // Transaksi diterima admin, barang sedang disewa.
    DISEWA("Disewa"),
    // Barang sudah dikembalikan dan transaksi selesai.
    SELESAI("Selesai");

    private final String label;

    StatusTransaksi(String label) {
        this.label = label;
    }

    // Mengambil teks status untuk ditampilkan ke layar.
    public String getLabel() {
        return label;
    }
}
