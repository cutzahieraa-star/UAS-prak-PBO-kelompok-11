package rental;

import java.util.ArrayList;

public class DaftarBarang {
    private final ArrayList<Barang> daftar;

    public DaftarBarang() {
        daftar = new ArrayList<>();
        isiDataAwal();
    }

    public void tambahBarang(Barang barang) {
        if (barang == null) throw new IllegalArgumentException("Barang tidak boleh null.");
        for (Barang b : daftar) {
            if (b.getId().equalsIgnoreCase(barang.getId())) {
                throw new IllegalArgumentException("ID " + barang.getId() + " sudah digunakan.");
            }
        }
        daftar.add(barang);
    }

    public Barang cariById(String id) throws BarangTidakDitemukanException {
        for (Barang barang : daftar) {
            if (barang.getId().equalsIgnoreCase(id)) return barang;
        }
        throw new BarangTidakDitemukanException("Barang dengan ID " + id + " tidak ditemukan.");
    }

    public void editBarang(String id, String nama, String ukuran, double tarifHarian)
            throws BarangTidakDitemukanException, BarangSedangDisewaException {
        Barang barang = cariById(id);
        if (!barang.isTersedia()) {
            throw new BarangSedangDisewaException("Barang " + id + " sedang disewa, tidak dapat diedit.");
        }
        barang.setNama(nama);
        barang.setUkuran(ukuran);
        barang.setTarifHarian(tarifHarian);
    }

    public void hapusBarang(String id)
            throws BarangTidakDitemukanException, BarangSedangDisewaException {
        Barang barang = cariById(id);
        if (!barang.isTersedia()) {
            throw new BarangSedangDisewaException("Barang " + id + " sedang disewa, tidak dapat dihapus.");
        }
        daftar.remove(barang);
    }

    public ArrayList<Barang> getBarangTersedia() {
        ArrayList<Barang> hasil = new ArrayList<>();
        for (Barang barang : daftar) {
            if (barang.isTersedia()) hasil.add(barang);
        }
        return hasil;
    }

    public ArrayList<Barang> getSemuaBarang() {
        return new ArrayList<>(daftar);
    }

    public void tampilkanSemua() {
        if (daftar.isEmpty()) {
            System.out.println("Belum ada barang dalam katalog.");
            return;
        }
        System.out.println("\n==================== DAFTAR BARANG ====================");
        for (Barang barang : daftar) {
            System.out.println(barang);
        }
        System.out.println("========================================================");
    }

    private void isiDataAwal() {
        tambahBarang(new BajuAdat("BA-001", "Ulee Balang", "L", 150000, "Aceh"));
        tambahBarang(new BajuAdat("BA-002", "Baju Kurung Melayu", "M", 120000, "Riau"));
        tambahBarang(new BajuAdat("BA-003", "Kebaya Sunda", "M", 130000, "Jawa Barat"));

        tambahBarang(new KostumKarakter("KC-001", "Kostum Naruto", "L", 100000, "Naruto", 30000));
        tambahBarang(new KostumKarakter("KC-002", "Kostum Sailor Moon", "M", 110000, "Sailor Moon", 35000));
        tambahBarang(new KostumKarakter("KC-003", "Kostum Spiderman", "L", 125000, "Spiderman", 40000));

        tambahBarang(new BusanaPengantin("BP-001", "Gaun Pengantin Putih", "M", 300000, 100000));
        tambahBarang(new BusanaPengantin("BP-002", "Baju Pengantin Adat Aceh", "L", 350000, 125000));
        tambahBarang(new BusanaPengantin("BP-003", "Gaun Pesta Elegan", "S", 250000, 75000));
    }
}
