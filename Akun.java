package rental;

/**
 * Kelas abstrak yang merepresentasikan akun pengguna dalam sistem rental busana.
 * Menjadi kelas induk bagi {@code Admin} dan {@code Penyewa}.
 *
 * @author Anggota 2
 * @version 1.0
 */
public abstract class Akun {
    private String id;
    private String username;
    private String password;

    /**
     * Konstruktor untuk menginisialisasi data dasar akun pengguna.
     *
     * @param id       ID unik akun
     * @param username nama pengguna untuk proses login
     * @param password kata sandi untuk proses login
     */
    public Akun(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Mengambil ID akun pengguna.
     * @return ID akun
     */
    public String getId() {
        return id;
    }

    /**
     * Mengambil username akun pengguna.
     * @return username akun
     */
    public String getUsername() {
        return username;
    }

    /**
     * Memverifikasi kecocokan username dan password yang dimasukkan saat login.
     * Password disimpan secara privat dan tidak pernah dicetak ke layar.
     *
     * @param username username yang diinput pengguna
     * @param password password yang diinput pengguna
     * @return {@code true} jika username dan password sesuai, {@code false} jika tidak
     */
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Menampilkan antarmuka menu CLI yang disesuaikan dengan peran masing-masing akun.
     */
    public abstract void tampilkanMenu();
}
