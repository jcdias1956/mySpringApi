package com.algaworks.algafood.domain.service;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.FotoProdutoNaoEcontradaException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.service.FotoStorageService.NovaFoto;

@Service
public class CatalogoFotoProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private FotoStorageService fotoStorageService;
	
	@Transactional
	public FotoProduto salvar(FotoProduto fotoProduto, InputStream dadosArquvo) {
		
		Long restauranteId = fotoProduto.getRestauranteId();
		Long produtoId = fotoProduto.getProduto().getId();
		
		String nomeArquivoNovaFoto = 
				fotoStorageService.geraNomeAquivoNovaFoto(fotoProduto.getNomeArquivo());
		
		String nomeArquivoExistente = null;
		
		Optional<FotoProduto> fotoExistente = produtoRepository
			.findFotoById(restauranteId, produtoId);
		
		if (fotoExistente.isPresent()) {
			nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
			produtoRepository.delete(fotoExistente.get());
		}
		
		fotoProduto.setNomeArquivo(nomeArquivoNovaFoto);
		fotoProduto = produtoRepository.save(fotoProduto);
		produtoRepository.flush();

		NovaFoto novaFoto = NovaFoto.builder()
				.nomeArquivo(fotoProduto.getNomeArquivo())
				.contentType(fotoProduto.getContentType())
				.inputStream(dadosArquvo)
				.build();
		
		fotoStorageService.substituir(nomeArquivoExistente, novaFoto);
		
		return fotoProduto;
		
	}
	
	@Transactional
	public void excluir(Long restauranteId, Long produtoId) {
		
		FotoProduto fotoProduto = buscarOuFalhar(restauranteId, produtoId);
		
		produtoRepository.delete(fotoProduto);
		produtoRepository.flush();
		
		fotoStorageService.remover(fotoProduto.getNomeArquivo());
	}
	
	public FotoProduto buscarOuFalhar(Long restauranteId, Long produtoId) {
		return produtoRepository.findFotoById(restauranteId, produtoId)
				.orElseThrow(() -> new FotoProdutoNaoEcontradaException(restauranteId, produtoId));
	}

}
