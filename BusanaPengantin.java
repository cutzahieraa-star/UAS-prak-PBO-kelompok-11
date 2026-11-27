package rental;

public class BusanaPengantin extends Barang {
    private double biayaFitting;

    public BusanaPengantin(String id, String nama, String ukuran,
                           double tarifHarian, double biayaFitting) {
        super(id, nama, ukuran, tarifHarian);
        setBiayaFitting(biayaFitting);
    }

    public double getBiayaFitting() { return biayaFitting; }

    public void setBiayaFitting(double biayaFitting) {
        if (biayaFitting < 0) {
            throw new IllegalArgumentException("Biaya fitting tidak boleh negatif.");
        }
        this.biayaFitting = biayaFitting;
    }

    @Override
    public String getKategori() { return "Busana Pengantin/Pesta"; }

    @Override
    public double hitungBiaya(int hari) {
        if (hari <= 0) throw new IllegalArgumentException("Lama sewa harus lebih dari 0 hari.");
        return (getTarifHarian() * hari) + biayaFitting;
    }

    @Override
    public String toString() {
        return super.toString() + " | Biaya fitting: Rp"
            + String.format("%,.0f", biayaFitting);
    }
}
