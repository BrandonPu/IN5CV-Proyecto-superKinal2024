-- drop database if  exists  superKinalDB;

create database if not exists superKinalDB;

use superKinalDB;

create table Cargos(
	cargoId int not null auto_increment,
    nombreCargo varchar(30) not null,
    descripcionCargo varchar(100) not null,
    primary key PK_cargoId (cargoId)
);

create table Empleados(
	empleadoId int not null auto_increment,
    nombreEmpleado varchar(30) not null,
    apellidoEmpleado varchar(30) not null,
    sueldo decimal(10,2) not null,
    horaEntrada time not null,
    horaSalida time not null,
    cargoId int not null,
    encargadoId int,
    primary key PK_empleadoId (empleadoId),
    constraint FK_encargadoId foreign key Empleados(encargadoId)
		references Empleados(empleadoId),
    constraint FK_Empleados_Cargos foreign key Cargos(cargoId)
		references Cargos (cargoId)
);

create table Distribuidores(
	distribuidorId int not null auto_increment,
    nombreDistribuidor varchar(30) not null,
    direccionDistribuidor varchar(200) not null,
    nitDistribuidor varchar(15) not null,
    telefonoDistribuidor varchar(15) not null,
    web varchar(50),
    primary key PK_distribuidorId (distribuidorId)
);

create table CategoriaProductos(
	categoriaProductosId int not null auto_increment,
    nombreCategoria varchar(30) not null,
    descripcionCategoria varchar(100) not null,
    Primary key PK_categoriaProductosId(categoriaProductosId)
);

create table Compras(
	compraId int not null auto_increment,
    fechaCompra date not null,
    totalCompra decimal(10, 2),
    primary key compraId(compraId)
);

create table Productos(
	productoId int not null auto_increment,
    nombreProducto varchar(50) not null,
    descripcionProducto varchar(100),
    cantidadStock int not null,
    precioVentaUnitario decimal(10,2) not null,
    precioVentaMayor decimal(10, 2) not null,
    precioCompra decimal(10, 2) not null,
    imagenProducto LONGBLOB,
    distribuidorId int not null,
    categoriaProductosId int not null,
    primary key PK_productoId(productoId),
    constraint FK_Productos_Distribuidores foreign key Distribuidores(distribuidorId)
	references Distribuidores(distribuidorId),
    constraint FK_Productos_CategoriaProductos foreign key CategoriaProductos(categoriaProductosId)
		references CategoriaProductos (categoriaProductosId)
);

create table DetalleCompra(
	detalleCompraId int not null auto_increment,
    cantidadCompra int not null,
    productoId int not null,
    compraId int not null,
    primary key PK_detalleCompraId (detalleCompraId),
    constraint FK_DetalleCompra_Productos foreign key (productoId)
		references Productos(productoId),
	constraint FK_DetalleCompra_Compras foreign key (compraId)
		references Compras(compraId)
);

create table Promociones(
	promocionId int not null auto_increment,
    precioPromocion decimal (10,2) not null,
    descripcionPromocion varchar (200),
    fechaInicio date not null,
    fechaFinalizacion date not null,
    productoId int not null,
    primary key PK_promocionId (promocionId),
    constraint FK_Promociones_Productos foreign key Promociones(productoId)
		references Productos (productoId)
);

create table Clientes(
	clienteId int not null auto_increment,
    nombre varchar(30) not null,
    apellido varchar(30) not null,
    telefono varchar(15),
    direccion varchar(150) not null,
    nit varchar(30) default 'CF',
    primary key PK_clienteId (clienteId)
);

create table Facturas(
	facturaId int not null auto_increment,
    fecha date not null,
    hora time not null,
    clienteId int not null,
    empleadoId int not null,
    total decimal(10,2),
    primary key PK_facturaId (facturaId),
    constraint FK_Facturas_Clientes foreign key Clientes(clienteId)
	references Clientes (clienteId),
    constraint FK_Facturas_Empleados foreign key Empleados(empleadoId)
		references Empleados (empleadoId)
);

create table DetalleFactura(
	detalleFacturaId int not null auto_increment,
    facturaId int not null,
    productoId int not null,
    primary key PK_detalleFacturaId(detalleFacturaId),
    constraint FK_DetalleFactura_Facturas foreign key Facturas(facturaId)
		references Facturas(facturaId),
    constraint FK_DetalleFactura_Productos foreign key Productos(productoId)
		references Productos(productoId)
);

create table TicketSoporte(
	ticketSoporteId int not null auto_increment,
    descripcionTicket varchar (250) not null,
    estatus varchar (30) not null,
    clienteId int not null,
    facturaId int,
	primary key PK_ticketSoporteId (ticketSoporteId),
    constraint FK_TicketSoporte_ClienteS foreign key TicketSoporte(clienteId)
		references Clientes (clienteId),
	constraint FK_TicketSoporte_Facturas foreign key TicketSoporte(facturaId)
		references Facturas (facturaId)
);

create table NivelesAcceso(
	nivelAccesoId int not null auto_increment,
    nivelAcceso varchar(40) not null,
    primary key PK_nivelAccesoId(nivelAccesoId)
);

create table Usuarios(
	usuarioId int not null auto_increment,
    usuario varchar(30) not null,
    contrasenia varchar(100) not null,
    nivelAccesoId int not null,
    empleadoId int not null,
    primary key PK_usuarioId(usuarioId),
    constraint FK_Usuarios_NivelesAcceso foreign key Usuarios(nivelAccesoId)
		references NivelesAcceso(nivelAccesoId),
	constraint FK_Usuarios_Empleados foreign key Usuarios(empleadoId)
		references Empleados(empleadoId)    
);
