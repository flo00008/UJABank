/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.restapi;

import es.ujaen.dae.ujabank.DTO.DTOCuenta;
import es.ujaen.dae.ujabank.DTO.DTOUsuario;
import es.ujaen.dae.ujabank.DTO.Mapper;
import es.ujaen.dae.ujabank.anotaciones.Login;
import es.ujaen.dae.ujabank.anotaciones.Logout;
import es.ujaen.dae.ujabank.beans.Banco;
import es.ujaen.dae.ujabank.entidades.Usuario;
import es.ujaen.dae.ujabank.excepciones.formato.TokenIncorrecto;
import es.ujaen.dae.ujabank.interfaces.ServiciosUsuario;
import es.ujaen.dae.ujabank.validators.TokenManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import es.ujaen.dae.ujabank.anotaciones.ValidarToken;
import javax.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author flo00008
 */
@RestController
@RequestMapping("/usuario")
@Validated
public class RESTUsuario {// implements ServiciosUsuario{

    @Autowired
    private Banco ujabank;

    @GetMapping("/test")
    public ResponseEntity comprobar() {
        return ResponseEntity.ok("API funciona correctamente (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧");
    }

//    @Override
    @PostMapping
    public ResponseEntity registrar(@RequestBody(required = true) DTOUsuario usuario) {
        ujabank.registrar(Mapper.usuarioMapper(usuario));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/cc")
    public ResponseEntity crearCuenta(@PathVariable("id") String dni, @ValidarToken @RequestParam String token) {
        ujabank.crearCuenta(dni);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @Overrider
    @GetMapping//(consumes = MediaType.APPLICATION_JSON_VALUE, poduces = MediaType.TEXT_PLAIN_VALUE)
    @Login
    public ResponseEntity login(@RequestBody(required = true) DTOUsuario usuarioDTO) {
        Usuario usuario = Mapper.usuarioMapper(usuarioDTO);
        boolean logeable = ujabank.login(usuario);

        UUID token = null;

        if (logeable) {
            token = UUID.randomUUID();
        }

        return ResponseEntity.ok(token);
    }

    @GetMapping("/{id}/logout")//como alternativa poner un put 
    @Logout
    public ResponseEntity logout(@ValidarToken @RequestParam String token) {
        //El aop ya lo elimina y solo se entra si el token existe

        return ResponseEntity.ok().build();
    }

//    @Override
    @GetMapping(value = "/{id}/cuentas")
    public ResponseEntity consultarCuentas(@PathVariable("id") String dni, @ValidarToken @RequestParam String token) {
        List<?> cuentas = ujabank.consultarCuentas(dni);
        
        return ResponseEntity.ok(cuentas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity borrar(@PathVariable("id") String dni) {
        try {
            ujabank.borrarUsuario(dni);
        } catch (IllegalArgumentException e) {
            throw new TokenIncorrecto();
        }

        return ResponseEntity.ok().build();
    }
}
