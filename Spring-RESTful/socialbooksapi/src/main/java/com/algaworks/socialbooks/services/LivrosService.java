package com.algaworks.socialbooks.services;

import com.algaworks.socialbooks.domain.Comentario;
import com.algaworks.socialbooks.domain.Livro;
import com.algaworks.socialbooks.repository.ComentariosRepository;
import com.algaworks.socialbooks.repository.LivrosRepository;
import com.algaworks.socialbooks.services.exceptions.AutorExistenteException;
import com.algaworks.socialbooks.services.exceptions.LivroExistenteException;
import com.algaworks.socialbooks.services.exceptions.LivroNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LivrosService {

    @Autowired
    private LivrosRepository livrosRepository;

    @Autowired
    private ComentariosRepository comentariosRepository;

    public List<Livro> listar() {
        return livrosRepository.findAll();
    }

    public Livro buscar(Long id) {
        Optional<Livro> livroOptional = livrosRepository.findById(id);
        if (!livroOptional.isPresent()) {
            throw new LivroNaoEncontradoException("O Livro não pode ser encontrado");
        }
        return livroOptional.get();
    }

    public Livro salvar(Livro livro) {
        if(livro.getId() != null){
            if(livrosRepository.findById(livro.getId()).isPresent()){
                throw new LivroExistenteException("O livro já existe");
            }
        }
        return livrosRepository.save(livro);
    }

    public void deletar(Long id) {
        try {
            livrosRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new LivroNaoEncontradoException("Livro não pôde ser encontrado.");
        }
    }

    public void atualizar(Livro livro) {
        verificarExistencia(livro);
        livrosRepository.save(livro);
    }

    public void verificarExistencia(Livro livro) {
        buscar(livro.getId());
    }

    public Comentario salvarComentario(Long idLivro, Comentario comentario) {
        Livro livro = buscar(idLivro);
        comentario.setLivro(livro);
        comentario.setData(new Date());
        return comentariosRepository.save(comentario);
    }

    public List<Comentario> listarComentarios(Long livroId) {
        Livro livro = buscar(livroId);
        return livro.getComentarios();
    }
}
