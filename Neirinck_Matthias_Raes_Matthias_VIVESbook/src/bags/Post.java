package bags;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Katrien.Deleu
 */
public class Post {

    private Integer id;
    private LocalDateTime datum;
    private String tekst;
    private String eigenaar;

    public Post() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public String getEigenaar() {
        return eigenaar;
    }

    public void setEigenaar(String eigenaar) {
        this.eigenaar = eigenaar;
    }

//    @Override
//    public String toString() {
//        return "Post{" + "id=" + id + ", datum=" + datum + ", tekst=" + tekst + ", eigenaar=" + eigenaar + '}';
//    }
    @Override
    public String toString() {
        return datum.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)) + " - " + eigenaar + " - " + tekst;
    }

}
