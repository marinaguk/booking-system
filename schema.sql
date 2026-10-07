--
-- PostgreSQL database dump
--

\restrict RVDTsZS6LBGvg9yMVtYzodMQo4zI4uPtgiSEuXFWWrEVyfrx5i0Z5ccqP27WGX4

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

-- Started on 2026-10-07 12:04:51

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 224 (class 1259 OID 16520)
-- Name: booking; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.booking (
    id integer NOT NULL,
    user_id integer,
    property_id integer,
    start_date date,
    end_date date
);


--
-- TOC entry 223 (class 1259 OID 16519)
-- Name: booking_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.booking_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4930 (class 0 OID 0)
-- Dependencies: 223
-- Name: booking_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.booking_id_seq OWNED BY public.booking.id;


--
-- TOC entry 222 (class 1259 OID 16448)
-- Name: property; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.property (
    property_id integer NOT NULL,
    property_name text,
    property_city text,
    property_price double precision,
    owner_id integer
);


--
-- TOC entry 221 (class 1259 OID 16447)
-- Name: property_property_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.property_property_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4931 (class 0 OID 0)
-- Dependencies: 221
-- Name: property_property_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.property_property_id_seq OWNED BY public.property.property_id;


--
-- TOC entry 220 (class 1259 OID 16438)
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id integer NOT NULL,
    name text,
    password text,
    role text DEFAULT 'user'::text
);


--
-- TOC entry 219 (class 1259 OID 16437)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4932 (class 0 OID 0)
-- Dependencies: 219
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 4768 (class 2604 OID 16523)
-- Name: booking id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.booking ALTER COLUMN id SET DEFAULT nextval('public.booking_id_seq'::regclass);


--
-- TOC entry 4767 (class 2604 OID 16451)
-- Name: property property_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.property ALTER COLUMN property_id SET DEFAULT nextval('public.property_property_id_seq'::regclass);


--
-- TOC entry 4765 (class 2604 OID 16441)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 4774 (class 2606 OID 16528)
-- Name: booking booking_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.booking
    ADD CONSTRAINT booking_pkey PRIMARY KEY (id);


--
-- TOC entry 4772 (class 2606 OID 16500)
-- Name: property property_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.property
    ADD CONSTRAINT property_pkey PRIMARY KEY (property_id);


--
-- TOC entry 4770 (class 2606 OID 16446)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4776 (class 2606 OID 16547)
-- Name: booking booking_property_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.booking
    ADD CONSTRAINT booking_property_id_fkey FOREIGN KEY (property_id) REFERENCES public.property(property_id) ON DELETE CASCADE;


--
-- TOC entry 4777 (class 2606 OID 16529)
-- Name: booking booking_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.booking
    ADD CONSTRAINT booking_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 4775 (class 2606 OID 16455)
-- Name: property fk_owner; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.property
    ADD CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES public.users(id);


-- Completed on 2026-10-07 12:04:51

--
-- PostgreSQL database dump complete
--

\unrestrict RVDTsZS6LBGvg9yMVtYzodMQo4zI4uPtgiSEuXFWWrEVyfrx5i0Z5ccqP27WGX4

