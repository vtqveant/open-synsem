package ru.eventflow.synsem.synsem;

/**
 * Poor man's ontology.
 */
public enum TBox {
    COMMAND("action"),
    THING("thing", new String[] {"Num", "Modifier", "Anchor"}),
    COLOR("q-color"),
    LOCATION("m-location", new String[] {"Anchor"});

    private String name;
    private String[] diamonds;

    private TBox(String name) {
        this(name, new String[] {});
    }

    private TBox(String name, String[] diamonds) {
        this.name = name;
        this.diamonds = diamonds;
    }

    public String getName() {
        return this.name;
    }

    public String[] getDiamonds() {
        return diamonds;
    }
}
