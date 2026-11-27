package rental;

// Exception saat checkout dilakukan dengan keranjang kosong.
public class KeranjangKosongException extends Exception {

    // Membuat exception dengan pesan kesalahan.
    public KeranjangKosongException(String pesan) {
        super(pesan);
    }
}

