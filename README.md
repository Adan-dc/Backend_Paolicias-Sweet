# Paolicias Sweet — Backend Microservices

Backend del proyecto **Paolicias Sweet**, desarrollado con **Spring Boot**, **MariaDB** y **Docker Compose** bajo una arquitectura de microservicios.

Este repositorio centraliza los siete servicios backend de la aplicación y permite levantar todo el entorno desde un único `docker-compose.yml`.

---

## Descripción

**Paolicias Sweet** es una aplicación orientada a la gestión de clientes, productos, reservas, pagos, costos y personal para eventos.

Cada microservicio posee una responsabilidad específica y utiliza su propia base de datos.

---

##  Microservicios

| Microservicio | Carpeta | Puerto |
|---|---|---:|
| Clientes | `Backend_Paolicias-Sweet-microservicio-clientes` | `8101` |
| Productos | `Backend_Paolicias-Sweet-microservicio-productos` | `8102` |
| Costos | `Backend-Service-Costo` | `8103` |
| Comprobante de Pago | `microservicio-comprobante-pago` | `8104` |
| Pago | `microservicio-pago` | `8105` |
| Reservas | `microservicio-reservas` | `8106` |
| Trabajadores / Personal | `Backend-Service-Personal` | `8107` |

---

## Comunicación entre servicios

Algunos microservicios necesitan consultar información de otros:

```text
Trabajadores  → Reservas
Costo         → Reservas
Costo         → Productos
Reservas      → Productos
Comprobante   → Pago
Pago          → Reservas
```

---

## Tecnologías

- Java 21
- Spring Boot
- Spring Data JPA
- MariaDB
- Maven
- Docker
- Docker Compose
- REST API
- Retrofit
- Git / GitHub

---


## Bases de datos

```text
paolicias_cliente
paolicias_producto
paolicias_costo
paolicias_comprobante_pago
paolicias_pago
paolicias_reserva
paolicias_trabajadores
```

Cada microservicio trabaja con su propia base de datos.

---

## Docker

### Construir todos los servicios

```bash
docker compose build
```

### Levantar todos los servicios

```bash
docker compose up -d
```

### Ver estado

```bash
docker compose ps
```

### Ver logs

```bash
docker compose logs
```

### Detener los servicios

```bash
docker compose down
```

---

Servicios disponibles:

```text
cliente
producto
costo
comprobante
pago
reservas
trabajadores
```
---

## 📱 Integración con Android

La aplicación Android utiliza Retrofit para consumir las APIs.

```kotlin
Retrofit.Builder()
    .baseUrl("https://nombredeldominio/paolicias/trabajadores/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

---

## Funcionalidades principales

- Registro y autenticación de clientes y empresas.
- Gestión de productos.
- Gestión de reservas.
- Gestión de pagos.
- Gestión de comprobantes de pago.
- Cálculo de costos.
- Registro, edición y eliminación de trabajadores.
- Asignación y cancelación de trabajadores en eventos.
- Comunicación entre microservicios.
- Persistencia con MariaDB.
- Despliegue con Docker Compose.
- Consumo desde aplicación Android mediante Retrofit.

---

## Paolicias Sweet

**Sistema de gestión de eventos desarrollado con Android, Spring Boot, MariaDB y Docker.**
