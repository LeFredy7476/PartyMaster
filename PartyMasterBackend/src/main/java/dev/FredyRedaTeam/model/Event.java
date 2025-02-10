package dev.FredyRedaTeam.model;

interface Event {
    String pack();
    void unpack(String cmd);
}
