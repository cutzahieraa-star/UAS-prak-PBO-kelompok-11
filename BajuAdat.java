package rental;

public class BajuAdat extends Barang {
    private String asalDaerah;

    public BajuAdat(String id, String nama, String ukuran,
                    double tarifHarian, String asalDaerah) {
        super(id, nama, ukuran, tarifHarian);
        setAsalDaerah(asalDaerah);
    }

    public String getAsalDaerah() { return asalDaerah; }

    public void setAsalDaerah(String asalDaerah) {
        if (asalDaerah == null || asalDaerah.trim().isEmpty()) {
            throw new IllegalArgumentException("Asal daerah tidak boleh kosong.");
        }
        this.asalDaerah = asalDaerah.trim();
    }

    @Override
    public String getKategori() { return "Baju Adat"; }

    @Override
    public double hitungBiaya(int hari) {
        validasiHari(hari);
        return getTarifHarian() * hari;
    }

    private void validasiHari(int hari) {
        if (hari <= 0) throw new IllegalArgumentException("Lama sewa harus lebih dari 0 hari.");
    }

    @Override
    public String toString() {
        return super.toString() + " | Asal: " + asalDaerah + " | " + getKategori();
    }
}
