output "instance_ip" {
  value = aws_instance.java_app.public_ip
}

output "private_key" {
  value     = tls_private_key.example.private_key_pem
  sensitive = true  # Marca o output como sensível para evitar exibição no console
}