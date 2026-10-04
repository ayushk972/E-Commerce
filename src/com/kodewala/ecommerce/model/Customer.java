package com.kodewala.ecommerce.model;

public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String mobile;
    private String address;

    public Customer(int customerId, String name, String email, String mobile, String address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
    }

    // Getters
	public int getCustomerId() {
		return customerId;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getMobile() {
		return mobile;
	}

	public String getAddress() {
		return address;
	}

    // Setter
	public void setAddress(String address) {
		this.address = address;
	}

    @Override
    public String toString() {
        return String.format(
            "+--------------------------------------------------+%n" +
            "| Customer ID  : %-33d|%n" +  // This Will print number in left side
            "| Name         : %-33s|%n" +  // If you have to print number in right side then remove - 
            "| Email        : %-33s|%n" +	// - "negative" means left right simple 33
            "| Mobile       : %-33s|%n" +
            "| Address      : %-33s|%n" +
            "+--------------------------------------------------+",
            customerId, name, email, mobile, address
        );
    }
}
