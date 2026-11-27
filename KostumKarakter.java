package rental;

public class KostumKarakter extends Barang {
    private String asalKarakter;
    private double biayaPerawatan;

    public KostumKarakter(String id, String nama, String ukuran,
                          double tarifHarian, String asalKarakter,
                          double biayaPerawatan) {
        super(id, nama, ukuran, tarifHarian);
        setAsalKarakter(asalKarakter);
        setBiayaPerawatan(biayaPerawatan);
    }

    public String getAsalKarakter() { return asalKarakter; }

    public void setAsalKarakter(String asalKarakter) {
        if (asalKarakter == null || asalKarakter.trim().isEmpty()) {
            throw new IllegalArgumentException("Asal karakter tidak boleh kosong.");
        }
        this.asalKarakter = asalKarakter.trim();
    }

    public double getBiayaPerawatan() { return biayaPerawatan; }

    public void setBiayaPerawatan(double biayaPerawatan) {
        if (biayaPerawatan < 0) {
            throw new IllegalArgumentException("Biaya perawatan tidak boleh negatif.");
        }
        this.biayaPerawatan = biayaPerawatan;
    }

    @Override
    public String getKategori() { return "Kostum Karakter/Cosplay"; }

    @Override
    public double hitungBiaya(int hari) {
        if (hari <= 0) throw new IllegalArgumentException("Lama sewa harus lebih dari 0 hari.");
        return (getTarifHarian() * hari) + biayaPerawatan;
    }

    @Override
    public String toString() {
        return super.toString() + " | Karakter: " + asalKarakter
            + " | Perawatan: Rp" + String.format("%,.0f", biayaPerawatan);
    }
}
