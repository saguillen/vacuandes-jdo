   SELECT punto.localizacion,COUNT(vacu.id) dosis
FROM A_USUARIO usua, A_CIUDADANO ciuda, a_puntovacunacion punto,a_vacunacion vacu
where punto.id= ciuda.idpuntov AND
      ciuda.id=vacu.idciudadano AND
      punto.id= vacu.idciudadano AND
      ciuda.estado='VACUNADO' AND
      vacu.fecha BETWEEN '01/january/2021' AND '31/december/2021'
      
      GROUP BY punto.localizacion
      ORDER BY COUNT(vacu.id) DESC
      FETCH FIRST 20 ROWS ONLY;
