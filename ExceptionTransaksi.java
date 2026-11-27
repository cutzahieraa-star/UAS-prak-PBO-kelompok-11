package rental;

// ExceptionTransaksi.java : PembayaranGagalException + TransaksiTidakValidException
// Exception yang digunakan ketika pembayaran gagal, seperti rekening kosong
// atau uang tunai yang dibayarkan kurang dari total.
class PembayaranGagalException extends Exception {
    private static final long serialVersionUID = 1L;

    // Membuat exception dengan pesan kesalahan.
    public PembayaranGagalException(String pesan) {
        super(pesan);
    }
}

// Exception yang digunakan ketika transaksi tidak valid, seperti lama sewa
// kurang dari 1 hari, transaksi tidak ditemukan, atau barang sedang disewa.
class TransaksiTidakValidException extends Exception {
    private static final long serialVersionUID = 1L;

    // Membuat exception dengan pesan kesalahan.
    public TransaksiTidakValidException(String pesan) {
        super(pesan);
    }
}