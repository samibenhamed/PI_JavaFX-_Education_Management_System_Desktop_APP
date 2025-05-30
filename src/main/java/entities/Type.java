package entities;

public enum Type {
    FRANCAIS,ANGLAIS,MATH,FINANCE,SPRINGBOOT,SYMFONY,JAVAFX,ARABE,SOA,RESEAUX;
    @Override
    public String toString() {
        switch (this) {
            case FRANCAIS: return "Français";
            case ANGLAIS: return "Anglais";
            case MATH: return "Mathématiques";
            case FINANCE: return "Finance";
            default: return name();
        }
    }

    public String toLowerCase() {
        return toString().toLowerCase();
    }
}
