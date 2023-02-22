package test;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import domain.Cliente;

import service.ClienteService;
import totalcross.ui.dialog.MessageBox;
import util.ValidadorCpfCnpj;


public class ClienteServiceTest {
	
	@Test
	public void deveRetornarClienteCriadoCorretamente() {
		
		String nome = "ClienteTeste";
		String email = "Teste@Teste.com.br";
		String telefone = "48-33445556";
		String tipoPessoa = "FISICA";
		String cpfCnpj = "04156821005";
		 
		Cliente clienteTeste = ClienteService.getInstance().createDomain(nome, email, telefone, cpfCnpj, tipoPessoa);
		Assert.assertEquals(nome , clienteTeste.nome);
		Assert.assertEquals(email , clienteTeste.email);
		Assert.assertEquals(telefone , clienteTeste.telefone);
		Assert.assertEquals(tipoPessoa , clienteTeste.tipoPessoa);
		Assert.assertEquals(cpfCnpj , clienteTeste.cpfCnpj);
	}
	
	@Test
	public void deveValidarCorretamenteTelefone() {
		String telefone = "48991169784";
		boolean validacao = ClienteService.getInstance().validateUpdate(telefone);
		Assert.assertTrue(validacao);
	}
	

	@Test
	public void deveValidarCorretamenteONumeroDoDoCpf() {
		String documento = "87273237060";
		boolean isValido = (ValidadorCpfCnpj.isCPF(documento));
		Assert.assertTrue(isValido);
		
	}
	@Test
	public void deveValidarCorretamenteONumeroDoDoCnpj() {
		String documento = "98613514000117";
		boolean isValido = (ValidadorCpfCnpj.isCNPJ(documento));
		Assert.assertTrue(isValido);
		
	}
	@Test
	public void deveDarErroAoInserirCnpjInvalido() {
		String documento = "884515787541512";
		boolean isValido = (ValidadorCpfCnpj.isCNPJ(documento));
		Assert.assertFalse(isValido);
		
	}
	@Test
	public void deveDarErroAoInserirCpfInvalido() {
		String documento = "07455223448";
		boolean isValido = (ValidadorCpfCnpj.isCPF(documento));
		Assert.assertFalse(isValido);
		
	}
	
}
