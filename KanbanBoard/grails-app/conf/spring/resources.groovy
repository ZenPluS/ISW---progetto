import kanbanboard.UtentiPasswordEncoderListener
// Place your Spring DSL code here
beans = {
    utentiPasswordEncoderListener(UtentiPasswordEncoderListener, ref('hibernateDatastore'))
}
