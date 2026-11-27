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
