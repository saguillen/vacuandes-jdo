SELECT usua.nombre
FROM A_USUARIO usua, A_CIUDADANO ciuda, a_puntovacunacion punto,a_vacunacion vacu
where punto.id= ciuda.idpuntov AND
      ciuda.idusuario=usua.id AND
      vacu.fecha BETWEEN '01/january/2021' AND '31/december/2021';