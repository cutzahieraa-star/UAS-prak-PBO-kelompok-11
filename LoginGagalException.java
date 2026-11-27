package rental;

/**
 * Kelas exception khusus untuk menangani kegagalan autentikasi,
 * yang dipicu apabila username atau password yang dimasukkan tidak cocok.
 *
 * @author Anggota 2
 * @version 1.0
 */
public class LoginGagalException extends Exception {

    /**
     * Menginisialisasi objek {@code LoginGagalException} beserta keterangan
     * alasan gagalnya proses login.
     *
     * @param pesan informasi rinci mengenai penyebab gagal login
     */
    public LoginGagalException(String pesan) {
        super(pesan);
    }
}
