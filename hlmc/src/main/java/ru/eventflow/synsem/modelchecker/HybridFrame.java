package ru.eventflow.synsem.modelchecker;


import java.util.*;

/**
 * Hybrid Kripke frame with various structures to store and resolve symbols.
 * Probably not the fastest possible, but provides enough flexibility to not bother with manual handling of collections.
 */
public class HybridFrame {

    /**
     * A name of the frame for messages
     */
    String name;

    /**
     * modality, from world, to world
     */
    Map<String, Map<String, Set<String>>> accessibilityRelations;

    /**
     * world, prop symbols true at world
     */
    Map<String, Set<String>> propositionalSymbolAssignments;

    /**
     * nominal to world (one to one)
     */
    Map<String, String> nominalAssignments;

    /**
     * world to nominals (one to many)
     */
    Map<String, Set<String>> nominalsAtWorld;

    /**
     * a list of known propositional symbols for faster lookups
     */
    Set<String> propositionalSymbols;

    /**
     * a list of known worlds for queries and checks
     */
    Set<String> worlds;

    public HybridFrame(String name) {
        accessibilityRelations = new HashMap<String, Map<String, Set<String>>>();
        propositionalSymbolAssignments = new HashMap<String, Set<String>>();
        nominalAssignments = new HashMap<String, String>();
        propositionalSymbols = new HashSet<String>();
        worlds = new HashSet<String>();
        nominalsAtWorld = new HashMap<String, Set<String>>();
        this.name = name;
    }

    boolean isModality(String label) {
        return accessibilityRelations.containsKey(label);
    }

    boolean isPropSym(String label) {
        return propositionalSymbols.contains(label);
    }

    boolean isNominal(String label) {
        return nominalAssignments.containsKey(label);
    }

    boolean isAccessible(String modalityLabel, String fromWorldLabel, String toWorldLabel) {
        return accessibilityRelations.containsKey(modalityLabel) &&
               accessibilityRelations.get(modalityLabel).containsKey(fromWorldLabel) &&
               accessibilityRelations.get(modalityLabel).get(fromWorldLabel).contains(toWorldLabel);
    }

    public void setAccessible(String modalityLabel, String fromWorldLabel, String toWorldLabel) {
        if (!accessibilityRelations.containsKey(modalityLabel)) {
            accessibilityRelations.put(modalityLabel, new HashMap<String, Set<String>>());
        }
        if (!accessibilityRelations.get(modalityLabel).containsKey(fromWorldLabel)) {
            accessibilityRelations.get(modalityLabel).put(fromWorldLabel, new HashSet<String>());
        }
        accessibilityRelations.get(modalityLabel).get(fromWorldLabel).add(toWorldLabel);
        if (!worlds.contains(fromWorldLabel)) worlds.add(fromWorldLabel);
        if (!worlds.contains(toWorldLabel)) worlds.add(toWorldLabel);
    }

    boolean isPropSymTrue(String propSymLabel, String worldLabel) {
        return propositionalSymbolAssignments.containsKey(worldLabel) &&
               propositionalSymbolAssignments.get(worldLabel).contains(propSymLabel);
    }

    public void setPropSym(String propSymLabel, String worldLabel) {
        if (!propositionalSymbolAssignments.containsKey(worldLabel)) {
            propositionalSymbolAssignments.put(worldLabel, new HashSet<String>());
        }
        propositionalSymbolAssignments.get(worldLabel).add(propSymLabel);
        if (!propositionalSymbols.contains(propSymLabel)) propositionalSymbols.add(propSymLabel);
    }

    String getNominalAssign(String nominalLabel) {
        return nominalAssignments.get(nominalLabel);
    }

    public void setNominalAssign(String nominalLabel, String worldLabel) {
        if (nominalAssignments.containsKey(nominalLabel) && !nominalAssignments.get(nominalLabel).equals(worldLabel)) {
            System.err.println("WARNING: reassigning a nominal " + nominalLabel + " to world " + worldLabel);
        }
        nominalAssignments.put(nominalLabel, worldLabel);

        // this is maintained for backward lookups
        if (!nominalsAtWorld.containsKey(worldLabel)) {
            nominalsAtWorld.put(worldLabel, new HashSet<String>());
        }
        nominalsAtWorld.get(worldLabel).add(nominalLabel);
    }

    public Set<String> getWorlds() {
        return worlds;
    }

    public Set<String> getWorldNominals(String worldLabel) {
        return nominalsAtWorld.get(worldLabel);
    }

    public Set<String> getWorldPropSymbols(String worldLabel) {
        return propositionalSymbolAssignments.get(worldLabel);
    }

    public void addWorld(String label) {
        worlds.add(label);
    }

    public String getName() {
        return name;
    }

    public Map<String, Map<String, Set<String>>> getAccessibilityRelations() {
        return accessibilityRelations;
    }
}
