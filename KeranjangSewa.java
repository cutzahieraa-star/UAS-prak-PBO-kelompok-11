package rental;

import java.util.ArrayList;

/**
 * Kelas untuk mengelola daftar barang sementara yang dipilih penyewa sebelum checkout.
 * Memastikan tidak ada barang duplikat atau barang yang sedang disewa masuk ke keranjang.
 *
 * @author Anggota 2
 * @version 1.0
 */
public class KeranjangSewa {
    private ArrayList<Barang> daftarBarang;

    /**
     * Konstruktor untuk membuat keranjang sewa baru dalam keadaan kosong.
     */
    public KeranjangSewa() {
        this.daftarBarang = new ArrayList<>();
    }

    /**
     * Mengambil daftar seluruh barang yang saat ini tersimpan di dalam keranjang.
     *
     * @return {@code ArrayList} berisi objek {@code Barang} di dalam keranjang
     */
    public ArrayList<Barang> getDaftarBarang() {
        return daftarBarang;
    }

    /**
     * Menambahkan objek barang ke dalam keranjang jika barang tersedia
     * dan belum ada di dalam keranjang.
     *
     * @param b objek {@code Barang} yang akan ditambahkan
     */
    public void tambahBarang(Barang b) {
        if (b == null) {
            System.out.println("Barang tidak ditemukan.");
            return;
        }
        if (!b.isTersedia()) {
            System.out.println("Gagal: Barang " + b.getNama() + " sedang disewa.");
            return;
        }
        for (Barang item : daftarBarang) {
            if (item.getId().equalsIgnoreCase(b.getId())) {
                System.out.println("Gagal: Barang " + b.getNama() + " sudah ada di keranjang.");
                return;
            }
        }
        daftarBarang.add(b);
        System.out.println("Berhasil menambahkan " + b.getNama() + " ke keranjang.");
    }

    /**
     * Menghapus barang dari keranjang berdasarkan ID barang.
     *
     * @param id ID unik barang yang akan dihapus dari keranjang
     */
    public void hapusBarang(String id) {
        for (int i = 0; i < daftarBarang.size(); i++) {
            if (daftarBarang.get(i).getId().equalsIgnoreCase(id)) {
                Barang dihapus = daftarBarang.remove(i);
                System.out.println("Berhasil menghapus " + dihapus.getNama() + " dari keranjang.");
                return;
            }
        }
        System.out.println("Barang dengan ID " + id + " tidak ditemukan di dalam keranjang.");
    }

    /**
     * Menghapus seluruh isi keranjang sewa setelah proses checkout selesai.
     */
    public void kosongkan() {
        daftarBarang.clear();
    }

    /**
     * Menampilkan daftar seluruh barang yang berada di dalam keranjang ke layar.
     */
    public void lihatIsi() {
        if (daftarBarang.isEmpty()) {
            System.out.println("Keranjang sewa Anda masih kosong.");
            return;
        }
        System.out.println("\n--- Isi Keranjang Sewa ---");
        for (Barang b : daftarBarang) {
            System.out.println("- " + b);
        }
    }
}
