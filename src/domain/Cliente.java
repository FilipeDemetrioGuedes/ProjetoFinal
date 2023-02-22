package domain;

public class Cliente {
	public static final String STATUS_PENDENTE = "PENDENTE" ;
	public static final String STATUS_OK = "OK" ;
	public static final String STATUS_PENDENTE_ATUALIZACAO = "PENDENTE ATUALIZAÇÃO" ;
	public static final String STATUS_ATUALIZADO = "ATUALIZADO" ;
	public static final String STATUS_PENDENTE_EXCLUSAO = "INATIVO";
	
	public String id;
	public String nome;
	public String email;
	public String telefone;
	public String tipoPessoa;
	public String cpfCnpj;
	public String status;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getTipoPessoa() {
		return tipoPessoa;
	}
	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}
	public String getCpfCnpj() {
		return cpfCnpj;
	}
	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}
	
}
