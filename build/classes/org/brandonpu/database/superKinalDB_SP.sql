use superKinalDB;

-- Asignar Total 
DELIMITER $$
create procedure sp_asignarTotal(in tot decimal(10,2), in facId int)
BEGIN
	update Facturas
		set total = tot
			where facturaId = facId;
END $$
DELIMITER ;

-- Buscar Usuario
DELIMITER $$
CREATE PROCEDURE sp_buscarUsuario(us varchar(30))
BEGIN
	select * from Usuarios
		where usuario = us;
END $$
DELIMITER ;

-- SP agregar Usuario
DELIMITER $$
CREATE PROCEDURE sp_agregarUsuario(us varchar(30), con varchar(100), nivAccId int, empId int)
BEGIN 
	INSERT INTO Usuarios(usuario,contrasenia,nivelAccesoId, empleadoId) values
		(us, con, nivAccId, empId);
END $$
DELIMITER ;


-- SP listar Nivel acceso
DELIMITER $$
CREATE PROCEDURE sp_listarNivelAcceso()
BEGIN
	select * from nivelesAcceso;
END $$
DELIMITER 

-- ----------------- CRUD ----------------- --
-- ******** Cargos ******** --
DELIMITER $$
CREATE PROCEDURE sp_agregarCargo(nom varchar(30),des varchar(100))
BEGIN
    INSERT INTO Cargos(nombreCargo, descripcionCargo)
    VALUES (nom, des);
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarCargos()
BEGIN
    SELECT * FROM Cargos;
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE sp_eliminarCargo(carId int)
BEGIN
    DELETE FROM Cargos WHERE cargoId = carId;
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE sp_buscarCargo(carId int)
BEGIN
    SELECT * FROM Cargos WHERE cargoId = carId;
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE sp_editarCargo(carId int,nom varchar(30),des varchar(100))
BEGIN
    UPDATE Cargos
    SET
        nombreCargo = nom,
        descripcionCargo = des
    WHERE cargoId = carId;
END $$
DELIMITER ;

-- ******** Empleados ******** --
DELIMITER $$
CREATE PROCEDURE sp_agregarEmpleado(nom varchar(30),ape varchar(30),sue decimal(10,2),he time,hs time,carId int)
BEGIN
    INSERT INTO Empleados(nombreEmpleado, apellidoEmpleado, sueldo, horaEntrada, horaSalida, cargoId)
    VALUES (nom, ape, sue, he, hs, carId);
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE sp_listarEmpleados()
BEGIN
    SELECT 
        EP.empleadoId, EP.nombreEmpleado, EP.apellidoEmpleado, EP.sueldo, EP.horaEntrada, EP.horaSalida, 
        CONCAT('Id: ', C.cargoId, ' | ', C.nombreCargo, ': ', C.descripcionCargo) AS 'cargo',
        CONCAT('Id: ', E.empleadoId, ' | ', E.nombreEmpleado ,' ', e.apellidoEmpleado) AS 'encargado' 
    FROM Empleados EP
    JOIN Cargos C ON EP.cargoId = C.cargoId
    LEFT JOIN Empleados E ON EP.encargadoId = E.empleadoId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_eliminarEmpleado(empId int)
BEGIN
    DELETE FROM Empleados WHERE empleadoId = empId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_buscarEmpleado(empId int)
BEGIN
    SELECT * FROM Empleados WHERE empleadoId = empId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_editarEmpleado(empId int,nom varchar(30),ape varchar(30),sue decimal(10,2),he time,hs time,carId int)
BEGIN
    UPDATE Empleados
    SET
        nombreEmpleado = nom,
        apellidoEmpleado = ape,
        sueldo = sue,
        horaEntrada = he,
        horaSalida = hs,
        cargoId  = carId
    WHERE empleadoId = empId;
END $$
DELIMITER ;

-- ******** Clientes ******** --
DELIMITER $$	
create procedure sp_agregarCliente(nom varchar(30), ape varchar(30), tel varchar(15), dir varchar(150), nit varchar(15))
begin
	If nit is null or nit = '' then
		set nit = 'CF';
	END IF;
    
	insert into Clientes(nombre, apellido, telefono, direccion, nit) values
		(nom, ape, tel, dir, nit);
end $$
DELIMITER ;

DELIMITER $$
create procedure sp_listarclientes()
begin
	select * from clientes;
end $$
DELIMITER ;

DELIMITER $$
create procedure sp_eliminarCliente(cliId int)
begin
	delete from Clientes
		where clienteId = cliId;
end $$
DELIMITER ;

DELIMITER $$
create procedure sp_buscarCliente(cliId int)
begin
	select * from Clientes
		where clienteId = cliId;
end $$
DELIMITER ;

DELIMITER $$
create procedure sp_editarCliente(cliId int, nom varchar(30), ape varchar(30), tel varchar(15), dir varchar(150), nt varchar(15))
begin
	update Clientes set
		nombre = nom,
        apellido = ape,
        telefono = tel,
        direccion = dir,
        nit = nt
			where clienteId = cliId;
end $$
DELIMITER ;

-- ******** Facturas ******** --
DELIMITER $$
CREATE PROCEDURE sp_agregarFactura(cliId int,empId int)
BEGIN
    INSERT INTO Facturas(fecha, hora, clienteId, empleadoId)
    VALUES (DATE(NOW()), TIME(NOW()), cliId, empId);
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarFacturas()
BEGIN
    SELECT F.facturaId, F.fecha, F.hora,
	CONCAT('Id: ', C.clienteId, ' | ', C.nombre, ' ', C.apellido) AS 'cliente',
	CONCAT('Id: ', E.empleadoId, ' | ', E.nombreEmpleado ,' ', e.apellidoEmpleado) AS 'empleado',
    F.total from Facturas F
    JOIN Clientes C on F.clienteId = C.clienteId
    JOIN Empleados E on F.empleadoId = E.empleadoId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_eliminarFactura(facId int)
BEGIN
    DELETE FROM Facturas WHERE facturaId = facId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_buscarFactura(facId int)
BEGIN
    SELECT * FROM Facturas WHERE facturaId = facId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_editarFactura(facId int,cliId int,empId int)
BEGIN
    UPDATE Facturas
    SET
        clienteId = cliId,
        empleadoId = empId
    WHERE facturaId = facId;
END $$
DELIMITER ;


-- ************* Asignar total *********** --
DELIMITER $$
CREATE PROCEDURE sp_editarTotalFactura(facId int, tot decimal(10,2))
BEGIN
    UPDATE Facturas
    SET
		total = tot
    WHERE facturaId = facId;
END $$
DELIMITER ;


-- ******** TicketSoporte ******** --
DELIMITER $$
CREATE PROCEDURE sp_agregarTicketSoporte(des varchar(250), cliId int, facId int)
BEGIN
    INSERT INTO TicketSoporte(descripcionTicket, estatus, clienteId, facturaId)
    VALUES (des, 'Recién Creado', cliId, facId);
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarTicketsSoporte()
BEGIN
    select TS.ticketSoporteId, TS.descripcionTicket, TS.estatus,
			CONCAT('Id: ', C.clienteId, ' | ', C.nombre, ' ', C.apellido) AS 'cliente',
            CONCAT('Id: ', F.facturaId, ' | fecha: ', F.fecha, ' ', F.hora) AS 'factura' from TicketSoporte TS
    join Clientes C on TS.clienteId = C.clienteId
    join Facturas F ON TS.facturaId = F.facturaId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_eliminarTicketSoporte(ticSopId int)
BEGIN
    DELETE FROM TicketSoporte WHERE ticketSoporteId = ticSopId;
END $$
DELIMITER ;

DELIMITER $$
create PROCEDURE sp_buscarTicketSoporte(ticSopId int)
BEGIN
    SELECT 
		TS.ticketSoporteId, TS.descripcionTicket, TS.estatus,
			CONCAT('Id: ', C.clienteId, ' | ', C.nombre, ' ', C.apellido) AS 'cliente',
            CONCAT('Id: ', F.facturaId, ' | fecha: ', F.fecha, ' ', F.hora) AS 'factura' from TicketSoporte TS
    join Clientes C on TS.clienteId = C.clienteId
    join Facturas F ON TS.facturaId = F.facturaId  WHERE ticketSoporteId = ticSopId;
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE sp_editarTicketSoporte(ticSopId int,des varchar(250),est varchar(30),cliId int,facId int)
BEGIN
    UPDATE TicketSoporte
    SET
        descripcionTicket = des,
        estatus = est,
        clienteId = cliId,
        facturaId = facId
    WHERE ticketSoporteId = ticSopId;
END $$
DELIMITER ;

 

-- *********** Distribuidores ************ --
DELIMITER $$
CREATE PROCEDURE sp_agregarDistribuidor(nom varchar(30),dir varchar(200),nit varchar(15),tel varchar(15),web varchar(50))
BEGIN
    INSERT INTO Distribuidores(nombreDistribuidor, direccionDistribuidor, nitDistribuidor, telefonoDistribuidor, web)
    VALUES (nom, dir, nit, tel, web);
END $$
DELIMITER 

DELIMITER $$
CREATE PROCEDURE sp_listarDistribuidores()
BEGIN
    SELECT * FROM Distribuidores;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_eliminarDistribuidor(disId int)
BEGIN
    DELETE FROM Distribuidores WHERE distribuidorId = disId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_buscarDistribuidor(disId int)
BEGIN
    SELECT * FROM Distribuidores WHERE distribuidorId = disId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_editarDistribuidor(disId int,nom varchar(30),dir varchar(200),nit varchar(15),tel varchar(15),web varchar(50))
BEGIN
    UPDATE Distribuidores
    SET
        nombreDistribuidor = nom,
        direccionDistribuidor = dir,
        nitDistribuidor = nit,
        telefonoDistribuidor = tel,
        web = web
    WHERE distribuidorId = disId;
END $$
DELIMITER ;


-- *********** Categoria Productos *********** --
DELIMITER $$
CREATE PROCEDURE sp_agregarCategoriaProducto(nom varchar(30),des varchar(100))
BEGIN
    INSERT INTO CategoriaProductos(nombreCategoria, descripcionCategoria)
    VALUES (nom, des);
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarCategoriaProductos()
BEGIN
    SELECT * FROM CategoriaProductos;
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE sp_eliminarCategoriaProducto(catId int)
BEGIN
    DELETE FROM CategoriaProductos WHERE categoriaProductosId = catId;
END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE sp_buscarCategoriaProducto(catId int)
BEGIN
    SELECT * FROM CategoriaProductos WHERE categoriaProductosId = catId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_editarCategoriaProducto(catId int,nom varchar(30),des varchar(100))
BEGIN
    UPDATE CategoriaProductos
    SET
        nombreCategoria = nom,
        descripcionCategoria = des
    WHERE categoriaProductosId = catId;
END $$
DELIMITER ;

-- --- ************** Productos *************** --
DELIMITER $$
CREATE PROCEDURE sp_agregarProducto(nom varchar(50),des varchar(100),cant int,pvu decimal(10,2),pvm decimal(10,2),pc decimal(10,2),img LONGBLOB,disId int,catId int)
BEGIN
    INSERT INTO Productos(nombreProducto, descripcionProducto, cantidadStock, precioVentaUnitario, precioVentaMayor, precioCompra, imagenProducto, distribuidorId, categoriaProductosId)
    VALUES (nom, des, cant, pvu, pvm, pc, img, disId, catId);
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarProductos()
BEGIN
     SELECT P.productoId, P.nombreProducto, P.descripcionProducto, P.cantidadStock, P.precioVentaUnitario, P.precioVentaMayor, P.precioCompra, P.imagenProducto,
        CONCAT('Id: ', D.distribuidorId, '| ',D.nombreDistribuidor)AS 'Distribuidor',
		CONCAT('Id: ', C.categoriaProductosId, '| ',C.nombreCategoria)AS 'Categoria'
		FROM Productos P
        join Distribuidores D on P.distribuidorId = D.distribuidorId
		join CategoriaProductos C on P.categoriaProductosId = C.categoriaProductosId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_eliminarProducto(prodId int)
BEGIN
    DELETE FROM Productos WHERE productoId = prodId;
END $$
DELIMITER ;

DELIMITER $$
create PROCEDURE sp_buscarProducto(proId int)
BEGIN
    SELECT P.productoId, P.nombreProducto, P.descripcionProducto, P.cantidadStock, P.precioVentaUnitario, P.precioVentaMayor, P.precioCompra, P.imagenProducto
		FROM Productos P
		WHERE productoId = proId;
END $$
DELIMITER ;

Delimiter $$
CREATE PROCEDURE sp_buscarProductoId(proId int)
BEGIN
	SELECT P.productoId, P.nombreProducto, P.descripcionProducto, P.cantidadStock, P.precioVentaUnitario, P.precioVentaMayor, P.precioCompra,
        CONCAT('Id: ', D.distribuidorId, '| ',D.nombreDistribuidor)AS 'Distribuidor',
		CONCAT('Id: ', C.categoriaProductosId, '| ',C.nombreCategoria)AS 'categoria'
		FROM Productos P
        join Distribuidores D on P.distribuidorId = D.distribuidorId
		join CategoriaProductos C on P.categoriaProductosId = C.categoriaProductosId WHERE productoId = proId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_editarProducto(prodId int,nom varchar(50),des varchar(100),cant int,pvu decimal(10,2),pvm decimal(10,2),pc decimal(10,2),img LONGBLOB,disId int,catId int)
BEGIN
    UPDATE Productos
    SET
        nombreProducto = nom,
        descripcionProducto = des,
        cantidadStock = cant,
        precioVentaUnitario = pvu,
        precioVentaMayor = pvm,
        precioCompra = pc,
        imagenProducto = img,
        distribuidorId = disId,
        categoriaProductosId = catId
    WHERE productoId = prodId;
END $$
DELIMITER ;

-- *********** Compras ************** --
DELIMITER $$
CREATE PROCEDURE sp_agregarCompra(tot decimal(10,2))
BEGIN
    INSERT INTO Compras(fechaCompra, totalCompra)
    VALUES (DATE(NOW()), tot);
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarCompras()
BEGIN
    SELECT * FROM Compras;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_eliminarCompra(comId int)
BEGIN
    DELETE FROM Compras WHERE compraId = comId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_buscarCompra(comId int)
BEGIN
    SELECT * FROM Compras WHERE compraId = comId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_editarCompra(comId int,fec date,tot decimal(10,2))
BEGIN
    UPDATE Compras
    SET
        fechaCompra = fec,
        totalCompra = tot
    WHERE compraId = comId;
END $$
DELIMITER ;

-- *********** DetalleCompra ********** --
DELIMITER $$
CREATE PROCEDURE sp_agregarDetalleCompra(cant int,prodId int,comId int)
BEGIN
    INSERT INTO DetalleCompra(cantidadCompra, productoId, compraId)
    VALUES (cant, prodId, comId);
END $$
DELIMITER ;

DELIMITER $$
create PROCEDURE sp_listarDetalleCompras()
BEGIN
    SELECT 
		DC.detalleCompraId, DC.cantidadCompra,
        CONCAT('Id: ',P.productoId ,'| ', P.nombreProducto)AS 'producto',
        CONCAT('Id: ',C.compraId, '| fecha: ', fechaCompra , ' | total: ' , totalCompra)AS 'compra'
			FROM DetalleCompra DC
            JOIN Productos P on DC.productoId = P.productoId
            JOIN Compras C on DC.compraId = C.compraId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_eliminarDetalleCompra(detComId int)
BEGIN
    DELETE FROM DetalleCompra WHERE detalleCompraId = detComId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_buscarDetalleCompra(detComId int)
BEGIN
    SELECT * FROM DetalleCompra WHERE detalleCompraId = detComId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_editarDetalleCompra(detComId int,cant int,prodId int,comId int)
BEGIN
    UPDATE DetalleCompra
    SET
        cantidadCompra = cant,
        productoId = prodId,
        compraId = comId
    WHERE detalleCompraId = detComId;
END $$
DELIMITER ;

-- ***************** Promociones ****************** --
DELIMITER $$
CREATE PROCEDURE sp_agregarPromocion(pre decimal(10,2),des varchar(100), fecFin date,prodId int)
BEGIN
    INSERT INTO Promociones(precioPromocion, descripcionPromocion, fechaInicio, fechaFinalizacion, productoId)
    VALUES (pre, des, DATE(NOW()), fecFin, prodId);
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarPromociones()
BEGIN
    select PS.promocionId, PS.precioPromocion, PS.descripcionPromocion, PS.fechaInicio, PS.fechaFinalizacion,
		CONCAT('Id: ',P.productoId, '| ',P.nombreProducto)AS 'producto' from Promociones PS
            join Productos P on PS.productoId = P.productoId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_eliminarPromocion(promId int)
BEGIN
    DELETE FROM Promociones WHERE promocionId = promId;
END $$
DELIMITER ;

DELIMITER $$
create PROCEDURE sp_buscarPromocion(promId int)
BEGIN
    SELECT PS.promocionId, PS.precioPromocion, PS.descripcionPromocion, PS.fechaInicio, PS.fechaFinalizacion,
		CONCAT('Id: ',P.productoId, '| ',P.nombreProducto)AS 'producto' from Promociones PS
            join Productos P on PS.productoId = P.productoId WHERE promocionId = promId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_editarPromocion(promId int,pre decimal(10,2),des varchar(100),fecIni date,fecFin date,prodId int)
BEGIN
    UPDATE Promociones
    SET
        precioPromocion = pre,
        descripcionPromocion = des,
        fechaInicio = fecIni,
        fechaFinalizacion = fecFin,
        productoId = prodId
    WHERE promocionId = promId;
END $$
DELIMITER ;

-- ******************* DetalleFacturas ************* --
DELIMITER $$
CREATE PROCEDURE sp_agregarDetalleFactura(facId int,prodId int)
BEGIN
    INSERT INTO DetalleFactura(facturaId, productoId)
    VALUES (facId, prodId);
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_listarDetalleFacturas()
BEGIN
    SELECT  DF.detalleFacturaId,
			CONCAT('Id: ', F.facturaId, ' | fecha: ' , F.fecha, ' ',F.hora) AS 'factura',
            CONCAT('Id: ',P.productoId, '| ', P.nombreProducto)AS 'producto'
	FROM DetalleFactura DF
	JOIN Facturas F on DF.facturaId = F.facturaId
    JOIN Productos P on DF.productoId = P.productoId;
END $$
DELIMITER ;
  
DELIMITER $$
CREATE PROCEDURE sp_eliminarDetalleFactura(detFacId int)
BEGIN
    DELETE FROM DetalleFactura WHERE detalleFacturaId = detFacId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_buscarDetalleFactura(detFacId int)
BEGIN
    SELECT * FROM DetalleFactura WHERE detalleFacturaId = detFacId;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_editarDetalleFactura(detFacId int,facId int,prodId int)
BEGIN
    UPDATE DetalleFactura
    SET
        facturaId = facId,
        productoId = prodId
    WHERE detalleFacturaId = detFacId;
END $$
DELIMITER ;

-- ***************** Asignar Encargado **********************
DELIMITER $$
CREATE PROCEDURE sp_asignarEncargado(empId int, encId int)
BEGIN
	UPDATE Empleados 
		set encargadoId = encId
			where empleadoId = empId;
END $$
DELIMITER ;
