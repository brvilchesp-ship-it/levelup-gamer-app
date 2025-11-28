
# Level-Up Gamer (Android · Kotlin · Jetpack Compose)

**Integrantes:** Brayan Vilches, (tu compañero)

## Descripción
App de catálogo gamer con carrito, puntos y descuentos DUOC. Incluye login/registro, animaciones, navegación, almacenamiento local y acceso a recursos nativos.

## Funcionalidades
- Catálogo de productos con imágenes y carrito.
- Descuento automático 20% para correos `@duocuc.cl`.
- Pago suma puntos (total/100) y muestra nivel (Bronze/Silver/Gold/Diamond).
- Login/registro con validaciones (nombre, correo y edad 18+).
- Persistencia local con **DataStore**.
- **Recursos nativos**: vibración (feedback), **Photo Picker** (imagen de perfil).
- **Animaciones**: `AnimatedVisibility`, `animateContentSize`.
- Estructura **MVVM** y lógica desacoplada de la UI.

## Cómo ejecutar
1. Abrir el proyecto en **Android Studio**.
2. Sincronizar Gradle.
3. Ejecutar en emulador o dispositivo.
4. Crear usuario (si usas correo `@duocuc.cl` verás el descuento). Agrega productos, abrir carrito y pagar.

## Tecnologías
Kotlin, Jetpack Compose, Navigation, DataStore, Coil.

## Notas
- Se requieren permisos normales de **INTERNET** y **VIBRATE** (ya declarados).
- Commits sugeridos: `feat(auth)`, `feat(cart)`, `feat(points)`, `feat(native)`.
