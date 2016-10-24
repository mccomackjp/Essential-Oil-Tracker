package essentailOils;

import java.util.Collection;
import java.util.Formatter;
import java.util.TreeSet;

/**
 * @author mccomackjp
 * @version 1.0
 * @created 16-Sep-2016 6:20:28 PM
 */
public class EssentialOil {

	private Collection<String> attributes;
	private Collection<String> clashes;
    private Collection<String> concentrations;
	private String name;
    private double pricePerOunce;
    private double dropPerOunceRatio;

	public EssentialOil(String name){
        setName(name);
		attributes = new TreeSet<>();
		clashes = new TreeSet<>();
        concentrations = new TreeSet<>();
        dropPerOunceRatio = 1/600;
        setPricePerOunce(0.00);
	}

    public boolean equals(String name){
        return this.name.replaceAll(" ", "").equalsIgnoreCase(name.replaceAll(" ", ""));
    }

    public boolean equals(EssentialOil oil){
        return this.equals(oil.getName());
    }

    public boolean containsClash(String clash){
        return clashes.contains(clash.toLowerCase());
    }

    public boolean containsAttribute(String attribute){
        return attributes.contains(attribute.toLowerCase());
    }

    public void addAttribute(String attribute){
        attributes.add(attribute.toLowerCase());
    }

    public void addClash(String clash){
        clashes.add(clash.toLowerCase());
    }

    public Collection<String> getAttributes() {
        return attributes;
    }

    public Collection<String> getClashes() {
        return clashes;
    }

    public Collection<String> getConcentrations() {
        return concentrations;
    }

    public void addConcentrations(String concetration) {
        concentrations.add(concetration);
    }

    public double getPricePerOunce() {
        return pricePerOunce;
    }

    public double getPricePerDrop(){
        return pricePerOunce * dropPerOunceRatio;
    }

    public void setPricePerOunce(double pricePerOunce) {
        this.pricePerOunce = pricePerOunce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}