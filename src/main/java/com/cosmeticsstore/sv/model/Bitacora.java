package com.cosmeticsstore.sv.model;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "bitacora")
public class Bitacora implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "username_attempt")
	private String usernameAttempt;

	@Column(name = "message")
	private String message;

	@Column(name = "attempted_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date attemptedAt;

	public Bitacora() {}

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public String getUsernameAttempt() { return usernameAttempt; }
	public void setUsernameAttempt(String usernameAttempt) { this.usernameAttempt = usernameAttempt; }
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	public Date getAttemptedAt() { return attemptedAt; }
	public void setAttemptedAt(Date attemptedAt) { this.attemptedAt = attemptedAt; }

}
