package rental;

/**
 * Exception yang dilempar ketika penyewa mencoba melakukan proses checkout
 * sementara keranjang sewa masih dalam keadaan kosong.
 *
 * @author Anggota 2
 * @version 1.0
 */
public class KeranjangKosongException extends Exception {

    /**
     * Membuat instance baru {@code KeranjangKosongException} dengan pesan kesalahan tertentu.
     *
     * @param pesan pesan detail yang menjelaskan bahwa keranjang masih kosong
     */
    public KeranjangKosongException(String pesan) {
        super(pesan);
    }
}
