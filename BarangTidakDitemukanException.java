package rental;

public class BarangTidakDitemukanException extends Exception {
    public BarangTidakDitemukanException(String pesan) {
        super(pesan);
    }
}
