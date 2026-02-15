# Red Social Universitaria - Trabajo Integrador

**Programación III - 2026**
Alumno: Lucas Miño
Grupo: LM
---

## 📋 Descripción del Proyecto

Sistema de red social universitaria que implementa 4 problemas algorítmicos diferentes, cubriendo los siguientes paradigmas de diseño de algoritmos:

Problema 1: Gestión de Publicaciones - **Divide y Conquista**
Problema 2: Asignación de Publicidad - **Programación Dinámica** 
Problema 3: Recomendación de Amigos - **Greedy** 
Problema 4: Simulación de Bloqueos - **Backtracking**

---

## 🏗️ Estructura del Proyecto

```
RedSocialUniversitaria/
│
├── src/
│   ├── modelo/                          
│   │   ├── Usuario.java                 
│   │   ├── Publicacion.java             
│   │   ├── Grafo.java                   
│   │   ├── Arista.java                  
│   │   └── Anuncio.java                                                  
│   │
│   ├── algoritmos/
│   │   ├── divideconquista/
│   │   │   └── GestorPublicaciones.java 
│   │   │
│   │   ├── programaciondinamica/
│   │   |   ├── ResultadoAsignacion.java
│   │   │   └── AsignadorPublicidad.java    
│   │   │
│   │   ├── greedy/
│   │   |   ├── RecomendadorAmigos.java 
│   │   |   ├── RecomendacionAmigo.java 
│   │   │   └── EstadisticasConectividad.java  
│   │   │
│   │   └── backtracking/
│   │       ├── SimuladorBloqueos.java
│   │       ├── ResultadoBloqueo.java 
│   │       └── ParUsuarios.java   
│   │
│   ├── test/
│   │   ├── TestSimuladorBloqueos.java 
│   │   ├── TestRecomendadorAmigos.java 
│   │   ├── TestGestorPublicaciones.java 
│   │   └── TestAsignadorPublicidad.java  
│   │
│   └── Main.java                         
│
└── docs/                   
    └── Informe_Tecnico.pdf                      
```

## 🧪 Testing

Cada problema incluye casos de prueba:

1. **Caso base** (2-3 elementos)
2. **Caso pequeño** (10-20 elementos)
3. **Caso mediano** (100-500 elementos)
4. **Caso grande** (1000-5000 elementos)
5. **Análisis de complejidad** (múltiples tamaños)
