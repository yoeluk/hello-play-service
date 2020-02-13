resolvers in ThisBuild += "lightbend-commercial-mvn" at
  "https://repo.lightbend.com/pass/BHPC7KuhJUO9e666DDldWUuVHXzUUFUInj2jT7MOrVopbuok/commercial-releases"
resolvers in ThisBuild += Resolver.url("lightbend-commercial-ivy",
  url("https://repo.lightbend.com/pass/BHPC7KuhJUO9e666DDldWUuVHXzUUFUInj2jT7MOrVopbuok/commercial-releases"))(Resolver.ivyStylePatterns)