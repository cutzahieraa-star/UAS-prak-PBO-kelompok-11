package rental;

public abstract class Barang {
    private String id;
    private String nama;
    private String ukuran;
    private double tarifHarian;
    private boolean tersedia;

    public Barang(String id, String nama, String ukuran, double tarifHarian) {
        setId(id);
        setNama(nama);
        setUkuran(ukuran);
        setTarifHarian(tarifHarian);
        this.tersedia = true;
    }

    public String getId() { return id; }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID tidak boleh kosong.");
        }
        this.id = id.trim().toUpperCase();
    }

    public String getNama() { return nama; }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama barang tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public String getUkuran() { return ukuran; }

    public void setUkuran(String ukuran) {
        if (ukuran == null || ukuran.trim().isEmpty()) {
            throw new IllegalArgumentException("Ukuran tidak boleh kosong.");
        }
        this.ukuran = ukuran.trim();
    }

    public double getTarifHarian() { return tarifHarian; }

    public void setTarifHarian(double tarifHarian) {
        if (tarifHarian <= 0) {
            throw new IllegalArgumentException("Tarif harian harus lebih dari 0.");
        }
        this.tarifHarian = tarifHarian;
    }

    public boolean isTersedia() { return tersedia; }

    public void setTersedia(boolean tersedia) { this.tersedia = tersedia; }

    public abstract String getKategori();

    public abstract double hitungBiaya(int hari);

    @Override
    public String toString() {
        return String.format(
            "%-8s | %-25s | %-8s | Rp%,.0f/hari | %s",
            id, nama, ukuran, tarifHarian, tersedia ? "Tersedia" : "Sedang disewa"
        );
    }
}
