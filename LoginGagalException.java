package rental;

/**
 * Exception yang dilempar ketika kredensial (username atau password)
 * tidak sesuai saat pengguna melakukan proses login.
 *
 * @author Anggota 2
 * @version 1.0
 */
public class LoginGagalException extends Exception {

    /**
     * Membuat instance baru {@code LoginGagalException} dengan pesan kesalahan tertentu.
     *
     * @param pesan pesan detail yang menjelaskan penyebab kegagalan login
     */
    public LoginGagalException(String pesan) {
        super(pesan);
    }
}
